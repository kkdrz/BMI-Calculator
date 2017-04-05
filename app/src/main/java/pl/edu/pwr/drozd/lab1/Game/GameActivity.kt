package pl.edu.pwr.drozd.lab1.Game

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.author_activity.*
import pl.edu.pwr.drozd.lab1.Game.Ball


class GameActivity : Activity() {

    lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView = GameView(this)
        setContentView(gameView)
        gameView.resume()
    }

    class GameView(context: Context) : SurfaceView(context), Runnable {

        val ourHolder: SurfaceHolder = holder
        val paint: Paint = Paint()
        var playing: Boolean = false
        var paused: Boolean = true
        var gameThread: Thread = Thread(this)
        var canvas: Canvas = Canvas()
        var fps: Long = 0
        var timeThisFrame: Long = 0
        val size : Point = Point()
        val screenX: Int
        val screenY: Int
        val ball: Ball

        init {
            val wManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wManager.defaultDisplay.getSize(size)
            this.screenX = size.x
            this.screenY = size.y

            Log.d("DEBUG", " ScreenX " + this.screenX)
            Log.d("DEBUG", " ScreenY " + this.screenY)

            this.ball = Ball (screenX, screenY)
            createBricksAndRestart()
        }


        override fun run() {
            while (playing) {
                val startFrameTime = System.currentTimeMillis()
                if (!paused) update()
                draw()
                timeThisFrame = System.currentTimeMillis() - startFrameTime
                if (timeThisFrame > 1) {
                    fps = 1000 / timeThisFrame
                }
            }
        }

        fun update() {
            ball.update(fps)

            // Bounce the ball back when it hits the bottom of screen
            if (ball.rect.bottom >= screenY) {
                ball.reverseYVelocity()
            }

            // Bounce the ball back when it hits the top of screen
            if (ball.rect.top <= 0) {
                ball.reverseYVelocity()
            }

            // If the ball hits left wall bounce
            if (ball.rect.left <= 0) {
                ball.reverseXVelocity()
            }

            if (ball.rect.right >= screenX) {
                ball.reverseXVelocity()
            }
        }

        fun draw() {
            if (ourHolder.surface.isValid) {
                canvas = ourHolder.lockCanvas()
                canvas.drawColor(Color.argb(255, 0, 219, 204))
                paint.color = Color.argb(255, 255, 54, 30)
                canvas.drawRect(ball.rect, paint)
                ourHolder.unlockCanvasAndPost(canvas)
            }
        }

        fun pause() {
            playing = false
            try {
                gameThread.join()
            } catch (e: InterruptedException) {
                Log.e("Error: ", "Joining thread - pause()")
            }
        }

        fun resume() {
            playing = true
            gameThread = Thread(this)
            gameThread.start()
        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {paused = false}
                MotionEvent.ACTION_UP -> { }
            }
            return true
        }

        fun createBricksAndRestart() {
            ball.reset()
        }

    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
        Log.d("MOJE", "PASUE")

    }
}
