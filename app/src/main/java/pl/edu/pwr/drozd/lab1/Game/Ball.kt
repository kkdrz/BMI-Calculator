package pl.edu.pwr.drozd.lab1.Game

import android.graphics.RectF

class Ball(val screenX: Int, val screenY: Int) {

    var xVelocity: Float = 200f
    var yVelocity: Float = -420f
    val width = 50f
    val ballHeight = 50f
    var rect : RectF = RectF((screenX/2).toFloat(), (screenY/2) + ballHeight, (screenX/2) + width, (screenY/2).toFloat())

    fun update(fps: Long) {
        rect.left = rect.left + (xVelocity / fps)
        rect.top = rect.top + (yVelocity / fps)
        rect.right = rect.left + width
        rect.bottom = rect.top - ballHeight
    }
    fun reverseYVelocity(){
        yVelocity = -yVelocity
    }

    fun reverseXVelocity() {
        xVelocity = -xVelocity
    }

    fun reset() {
        this.rect = RectF((screenX/2).toFloat(), (screenY/2) + ballHeight, (screenX/2) + width, (screenY/2).toFloat())
    }
}