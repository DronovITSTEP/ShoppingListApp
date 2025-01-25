package top.academy.shoppinglistapp.swipe;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Перечисление состояний кнопок.
 */
enum ButtonsState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

/**
 * Контроллер свайпов для RecyclerView, который позволяет отображать кнопки при свайпе элементов.
 */
public class SwipeController extends ItemTouchHelper.Callback {

    private boolean swipeBack = false;

    private ButtonsState buttonShowedState = ButtonsState.GONE;

    private RectF buttonInstance = null;

    private RecyclerView.ViewHolder currentItemViewHolder = null;

    private SwipeControllerActions buttonsActions = null;

    private static final float buttonWidth = 300;

    /**
     * Конструктор для SwipeController.
     *
     * @param buttonsActions Действия, которые будут выполнены при нажатии на кнопки.
     */
    public SwipeController(SwipeControllerActions buttonsActions) {
        this.buttonsActions = buttonsActions;
    }

    /**
     * Возвращает флаги движения для элемента RecyclerView.
     *
     * @param recyclerView RecyclerView, в котором происходит движение.
     * @param viewHolder ViewHolder элемента, для которого определяются флаги движения.
     * @return Флаги движения для элемента.
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    /**
     * Обрабатывает перемещение элемента в RecyclerView.
     *
     * @param recyclerView RecyclerView, в котором происходит перемещение.
     * @param viewHolder ViewHolder элемента, который перемещается.
     * @param target ViewHolder целевого элемента.
     * @return Всегда возвращает false, так как перемещение элементов не поддерживается.
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Обрабатывает свайп элемента в RecyclerView.
     *
     * @param viewHolder ViewHolder элемента, который был свайпнут.
     * @param direction Направление свайпа.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Пустой метод, так как свайп обрабатывается в onChildDraw
    }

    /**
     * Преобразует флаги в абсолютное направление.
     *
     * @param flags Флаги направления.
     * @param layoutDirection Направление макета.
     * @return Абсолютное направление.
     */
    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    /**
     * Обрабатывает рисование элемента при свайпе.
     *
     * @param c Canvas для рисования.
     * @param recyclerView RecyclerView, в котором происходит свайп.
     * @param viewHolder ViewHolder элемента, который свайпается.
     * @param dX Смещение по оси X.
     * @param dY Смещение по оси Y.
     * @param actionState Состояние действия.
     * @param isCurrentlyActive Флаг, указывающий, активен ли элемент в данный момент.
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
    }

    /**
     * Устанавливает обработчик касаний для элемента при свайпе.
     *
     * @param c Canvas для рисования.
     * @param recyclerView RecyclerView, в котором происходит свайп.
     * @param viewHolder ViewHolder элемента, который свайпается.
     * @param dX Смещение по оси X.
     * @param dY Смещение по оси Y.
     * @param actionState Состояние действия.
     * @param isCurrentlyActive Флаг, указывающий, активен ли элемент в данный момент.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
            if (swipeBack) {
                if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                else if (dX > buttonWidth) buttonShowedState  = ButtonsState.LEFT_VISIBLE;

                if (buttonShowedState != ButtonsState.GONE) {
                    setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    setItemsClickable(recyclerView, false);
                }
            }
            return false;
        });
    }

    /**
     * Устанавливает обработчик касаний для элемента при нажатии.
     *
     * @param c Canvas для рисования.
     * @param recyclerView RecyclerView, в котором происходит свайп.
     * @param viewHolder ViewHolder элемента, который свайпается.
     * @param dX Смещение по оси X.
     * @param dY Смещение по оси Y.
     * @param actionState Состояние действия.
     * @param isCurrentlyActive Флаг, указывающий, активен ли элемент в данный момент.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            return false;
        });
    }

    /**
     * Устанавливает обработчик касаний для элемента при отпускании.
     *
     * @param c Canvas для рисования.
     * @param recyclerView RecyclerView, в котором происходит свайп.
     * @param viewHolder ViewHolder элемента, который свайпается.
     * @param dX Смещение по оси X.
     * @param dY Смещение по оси Y.
     * @param actionState Состояние действия.
     * @param isCurrentlyActive Флаг, указывающий, активен ли элемент в данный момент.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener((v1, event1) -> false);
                setItemsClickable(recyclerView, true);
                swipeBack = false;

                if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                    if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                        buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
                    }
                    else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                        buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
                    }
                }
                buttonShowedState = ButtonsState.GONE;
                currentItemViewHolder = null;
            }
            return false;
        });
    }

    /**
     * Устанавливает кликабельность элементов в RecyclerView.
     *
     * @param recyclerView RecyclerView, в котором происходит свайп.
     * @param isClickable Флаг, указывающий, должны ли элементы быть кликабельными.
     */
    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    /**
     * Рисует кнопки на элементе при свайпе.
     *
     * @param c Canvas для рисования.
     * @param viewHolder ViewHolder элемента, который свайпается.
     */
    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
        p.setColor(Color.BLUE);
        c.drawRoundRect(leftButton, corners, corners, p);
        drawText("Изменить", c, leftButton, p);

        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        p.setColor(Color.RED);
        c.drawRoundRect(rightButton, corners, corners, p);
        drawText("Удалить", c, rightButton, p);

        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstance = leftButton;
        }
        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = rightButton;
        }
    }

    /**
     * Рисует текст на кнопке.
     *
     * @param text Текст для отображения.
     * @param c Canvas для рисования.
     * @param button RectF кнопки.
     * @param p Paint для рисования текста.
     */
    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    }

    /**
     * Обрабатывает рисование на холсте RecyclerView.
     *
     * @param c Canvas для рисования.
     */
    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }
}
