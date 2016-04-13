package ru.jin35.blind;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Toast;
import ru.jin35.blind.level.Level;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class Main extends AppCompatActivity {

    private static final float USER_SCALE_FACTOR = 0.8f;
    private static final int VALID_MOVE_DURATION = 400;
    private static final int INVALID_MOVE_DURATION = 200;
    private MapView mapView;
    private View user;
    private Map map;
    private Level level;
    private int cellSize;
    private int wallSize;
    private Vibrator vibrator;
    private LightView lightView;
    private View finish;
    private GestureListener gestureListener;
    private CompositeSubscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        LevelFactory.resetProgress(this);

        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.map);
        lightView = (LightView) findViewById(R.id.light);
        user = findViewById(R.id.user);
        finish = findViewById(R.id.finish);
        gestureListener = (GestureListener) findViewById(R.id.gesture);

        readLevel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unsubscribeAll();
    }

    private void subscribe() {
        unsubscribeAll();
        subscription = new CompositeSubscription();
        Observable<Move> gesturesObservable = gestureListener.getGesturesObservable();
        subscription.add(
                gesturesObservable
                        .filter(new ValidFilter(true, map))
                        .lift(new OperatorTimeFilter<Move>(VALID_MOVE_DURATION))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Move>() {
                            @Override
                            public void call(Move move) {
                                doValidMove(move);
                            }
                        }));
        subscription.add(
                gesturesObservable
                        .filter(new ValidFilter(false, map))
                        .lift(new OperatorTimeFilter<Move>(INVALID_MOVE_DURATION))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Move>() {
                            @Override
                            public void call(Move move) {
                                doInvalidMove(move);
                            }
                        }));
        subscription.add(
                mapView
                        .getObservable()
                        .subscribe(new Action1<int[]>() {
                            @Override
                            public void call(int[] ints) {
                                scaleViewsToMap(ints);
                            }
                        })
        );
    }

    private void unsubscribeAll() {
        if (subscription != null) {
            subscription.clear();
        }
    }

    private void readLevel() {
        level = LevelFactory.getLevel(this);
        map = level.getMap();
        mapView.setMap(map);
        lightView.setMap(map);
    }

    private void scaleViewsToMap(int[] ints) {
        cellSize = ints[0];//mapView.getCellSize();
        wallSize = ints[1];//mapView.getWallSize();
        //        int[] mapTranslation = mapView.getTranslation();
        int mapTranslationX = ints[2];
        int mapTranslationY = ints[3];

        lightView.setSizes(cellSize, wallSize, mapTranslationX, mapTranslationY);

        float scale = (float) cellSize / (float) user.getWidth();
        scale *= USER_SCALE_FACTOR;

        scaleView(user, scale);
        scaleView(finish, scale);

        int translationX = mapTranslationX + wallSize;
        int translationY = mapTranslationY + wallSize;

        float scaleTranslate = (1f - USER_SCALE_FACTOR) / 2f * (float) cellSize;
        translationX += scaleTranslate;
        translationY += scaleTranslate;

        user.setTranslationX(translationX);
        user.setTranslationY(translationY);

        int[] finishPosition = new int[2];
        map.getFinishPosition(finishPosition);
        translationX += ((finishPosition[1] - 1) / 2) * (cellSize + wallSize);
        translationY += ((finishPosition[0] - 1) / 2) * (cellSize + wallSize);
        finish.setTranslationX(translationX);
        finish.setTranslationY(translationY);
    }

    private void scaleView(View view, float scale) {
        view.setPivotX(0);
        view.setPivotY(0);
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    private void doValidMove(Move move) {
        ViewPropertyAnimator animator = user.animate();
        float translateSize = (float) cellSize + (float) wallSize;
        switch (move) {
            case UP:
                animator.translationYBy(-translateSize);
                break;
            case RIGHT:
                animator.translationXBy(translateSize);
                break;
            case DOWN:
                animator.translationYBy(translateSize);
                break;
            case LEFT:
                animator.translationXBy(-translateSize);
                break;
        }
        animator
                .setDuration(VALID_MOVE_DURATION);

        move.changePosition(map);
        if (map.hasFinish(map.position)) {
            scheduleGotoNextLevel();
        }

        vibrator.vibrate(VALID_MOVE_DURATION);
    }

    private void scheduleGotoNextLevel() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean complete = LevelFactory.onLevelComplete(Main.this, level);
                if (complete) {
                    Toast.makeText(Main.this, R.string.level_complete, Toast.LENGTH_LONG).show();
                    readLevel();
                    subscribe();
                } else {
                    Toast.makeText(Main.this, R.string.game_complete, Toast.LENGTH_LONG).show();
                }
            }
        }, VALID_MOVE_DURATION * 2);
    }

    private void doInvalidMove(Move move) {
        float translateSize = (float) cellSize * (1 - USER_SCALE_FACTOR) / 2f;
        final float moveSize;
        final boolean moveByX;
        switch (move) {
            case UP:
                moveSize = -translateSize;
                moveByX = false;
                break;
            case DOWN:
                moveSize = translateSize;
                moveByX = false;
                break;
            case LEFT:
                moveSize = -translateSize;
                moveByX = true;
                break;
            default:
            case RIGHT:
                moveSize = translateSize;
                moveByX = true;
                break;
        }

        final long moveDuration = INVALID_MOVE_DURATION / 3;
        final ViewPropertyAnimator animator = user
                .animate()
                .setDuration(moveDuration);
        animator.setListener(new OptionalAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ViewPropertyAnimator animator = user
                        .animate()
                        .setDuration(moveDuration)
                        .setStartDelay(moveDuration);
                if (moveByX) {
                    animator.translationXBy(-moveSize);
                } else {
                    animator.translationYBy(-moveSize);
                }
                animator.setListener(null);
            }
        });
        if (moveByX) {
            animator.translationXBy(moveSize);
        } else {
            animator.translationYBy(moveSize);
        }
        vibrator.vibrate(new long[]{0, moveDuration, moveDuration, moveDuration}, -1);
    }
}
