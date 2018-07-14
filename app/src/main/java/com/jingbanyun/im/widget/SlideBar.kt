package com.jingbanyun.im.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.jingbanyun.im.R
import org.jetbrains.anko.sp

class SlideBar(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var sectionHeight = 0f
    private var textBaseLine = 0f

    var onSectionListener: OnSectionChangeListener? = null

    init {
        paint.apply {
            color = resources.getColor(R.color.qq_section_text_color)
            textSize = sp(12).toFloat()
            textAlign = Paint.Align.CENTER
        }
    }

    companion object {
        // 26个字母
        private val SECTIONS = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#")
        private val paint = Paint()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //计算每个字符分配的高度
        sectionHeight = h * 1.0f / SECTIONS.size
        val fontMetrics = paint.fontMetrics
        //计算绘制文本的高度
        val textHeight = fontMetrics.descent - fontMetrics.ascent
        //计算基准线baseLine
        textBaseLine = sectionHeight / 2 + textHeight / 2 - fontMetrics.descent
    }

    override fun onDraw(canvas: Canvas) {
        //绘制所有的字母
        val x = width * 1.0f / 2//绘制字符的起始位置
        var baseline = textBaseLine
        SECTIONS.forEach {
            canvas.drawText(it, x, baseline, paint)
            baseline += sectionHeight
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                setBackgroundResource(R.drawable.bg_slide_bar)
                //找到点击的字母
                val index = getTouchIndex(event)
                val firstLetter = SECTIONS[index]
                println(firstLetter)
                onSectionListener?.onSectionChange(firstLetter)
            }
            MotionEvent.ACTION_MOVE -> {
                //找到点击的字母
                val index = getTouchIndex(event)
                val firstLetter = SECTIONS[index]
                println(firstLetter)
                onSectionListener?.onSectionChange(firstLetter)
            }
            MotionEvent.ACTION_UP -> {
                setBackgroundColor(Color.TRANSPARENT)
                onSectionListener?.onSlideFinish()
            }

        }
        return true//消费事件
    }

    //获取到点击位置的字母的下标
    private fun getTouchIndex(event: MotionEvent): Int {
        var index = (event.y / sectionHeight).toInt()
        //越界检查
        if (index < 0) {
            index = 0
        } else if (index >= SECTIONS.size) {
            index = SECTIONS.size - 1
        }
        return index
    }

    interface OnSectionChangeListener {
        fun onSectionChange(firstLetter: String)
        fun onSlideFinish()//滑动结束回调
    }
}