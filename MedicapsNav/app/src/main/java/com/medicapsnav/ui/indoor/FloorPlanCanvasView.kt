package com.medicapsnav.ui.indoor

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.medicapsnav.R

/**
 * Custom canvas view that renders a schematic floor plan.
 * Rooms are placed using relative coordinates (0.0 – 1.0).
 */
class FloorPlanCanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var rooms: List<RoomParcel> = emptyList()
    private var highlightedRoomId: String? = null

    // Paints
    private val wallPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#CFD8DC")
        style = Paint.Style.FILL
    }
    private val roomPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E3F2FD")
        style = Paint.Style.FILL
    }
    private val highlightPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#BBDEFB")
        style = Paint.Style.FILL
    }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1565C0")
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    private val highlightStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F44336")
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#0D47A1")
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }
    private val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    private val roomWidth  = 0.18f   // fraction of canvas width
    private val roomHeight = 0.12f   // fraction of canvas height

    fun setRooms(rooms: List<RoomParcel>) {
        this.rooms = rooms
        invalidate()
    }

    fun highlightRoom(roomId: String) {
        highlightedRoomId = roomId
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()

        // Floor background
        canvas.drawRect(0f, 0f, w, h, wallPaint)
        canvas.drawRect(16f, 16f, w - 16f, h - 16f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FAFAFA"); style = Paint.Style.FILL
        })

        // Draw corridor lines (horizontal and vertical center)
        val corridorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E0E0E0")
            strokeWidth = 2f
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(8f, 6f), 0f)
        }
        canvas.drawLine(0f, h / 2f, w, h / 2f, corridorPaint)
        canvas.drawLine(w / 2f, 0f, w / 2f, h, corridorPaint)

        val rw = w * roomWidth
        val rh = h * roomHeight
        textPaint.textSize = (rh * 0.2f).coerceAtLeast(12f)
        emojiPaint.textSize = (rh * 0.3f).coerceAtLeast(18f)

        rooms.forEach { room ->
            val cx = w * room.relativeX
            val cy = h * room.relativeY
            val rect = RectF(cx - rw / 2, cy - rh / 2, cx + rw / 2, cy + rh / 2)

            val isHighlighted = room.id == highlightedRoomId
            canvas.drawRoundRect(rect, 12f, 12f,
                if (isHighlighted) highlightPaint else roomPaint)
            canvas.drawRoundRect(rect, 12f, 12f,
                if (isHighlighted) highlightStrokePaint else strokePaint)

            // Emoji
            canvas.drawText(room.emoji, cx, cy - rh * 0.05f, emojiPaint)
            // Label (wrap long names)
            val label = if (room.name.length > 14) room.name.take(13) + "…" else room.name
            canvas.drawText(label, cx, cy + rh * 0.35f, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val w = width.toFloat()
            val h = height.toFloat()
            val rw = w * roomWidth
            val rh = h * roomHeight
            val tx = event.x
            val ty = event.y

            val tapped = rooms.firstOrNull { room ->
                val cx = w * room.relativeX
                val cy = h * room.relativeY
                tx in (cx - rw / 2)..(cx + rw / 2) &&
                ty in (cy - rh / 2)..(cy + rh / 2)
            }
            if (tapped != null) {
                highlightRoom(tapped.id)
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
