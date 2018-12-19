package com.example.jose_.simondice

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.adri.simondice.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    // Array de colores
    val colores = arrayOf("Red", "Green", "Yellow", "Blue")
    // Array del jugador
    var arrayPlayer = ArrayList<String>()
    // Array de la maquina
    var arrayMaquina = ArrayList<String>()
    // Clase random que obtiene el tiempo real de la maquina
    val random = Random(System.currentTimeMillis())
    // Dificultad de la partida
    var contadorDificultad = 1

    fun showColor(color: String, contador: Long) {
        // Hilo 1
        val handler = Handler()
        // Hilo 2
        val handler2 = Handler()
        // Valores audio
        val mpAmarillo = MediaPlayer.create(this, R.raw.amarillo_sound)
        val mpAzul = MediaPlayer.create(this, R.raw.azul_sound)
        val mpVerde = MediaPlayer.create(this, R.raw.verde_sound)
        val mpRojo = MediaPlayer.create(this, R.raw.rojo_sound)
        // Iniciamos el filtro con el primer hilo
        handler.postDelayed({
            when (color) {
                // Filtramos por String de color
                "Yellow" -> {
                    // En el primer hilo ponemos a true el boton
                    Yellow.isPressed = true
                    // Iniciamos el audio
                    mpAmarillo.start()
                    // Segundo hilo
                    handler2.postDelayed({
                        // Dentro del primer hilo creamos un segundo y ponemos el boton a false
                        Yellow.isPressed = false
                    }, 500) // Es importante tener diferentes tiempos entre el primer hilo y el segundo
                }

                "Blue" -> {
                    Blue.isPressed = true
                    mpAzul.start()
                    handler2.postDelayed({
                        Blue.isPressed = false
                    }, 500)
                }

                "Red" -> {
                    Red.isPressed = true
                    mpRojo.start()
                    handler2.postDelayed({
                        Red.isPressed = false
                    }, 500)
                }

                "Green" -> {
                    Green.isPressed = true
                    mpVerde.start()
                    handler2.postDelayed({
                        Green.isPressed = false
                    }, 500)
                }
            }
        }, 1000 * contador)
        // Es importante darse cuenta que hay que incrementar el tiempo del primer hilo, si no visualmente cambiarían todos los botones de estado a la vez
    }

    // Funcion para generar numeros aleatorios
    fun rand(from: Int, to: Int): Int {
        return random.nextInt(to - from) + from
    }

    // Comprueba si el tamaño es el mismo para comprobar el resultado de la partida
    fun compruebaTrigger() {
        if (arrayPlayer.size == arrayMaquina.size) {
            comprobar()
        }
    }

    // OnClick
    fun colorButtonPressed(view: View) {
        when (view) {
            view.Blue -> {
                arrayPlayer.add("Blue")
                compruebaTrigger()
            }

            view.Red -> {
                arrayPlayer.add("Red")
                compruebaTrigger()
            }

            view.Yellow -> {
                arrayPlayer.add("Yellow")
                compruebaTrigger()
            }

            view.Green -> {
                arrayPlayer.add("Green")
                compruebaTrigger()
            }
        }
    }

    // Funcion para generar Strings aleatorias
    fun randomString(): String {
        return colores[rand(0, 3)]
    }

    // Comprueba el resultado de la partida
    fun comprobar() {
        disableButtons()
        if (arrayPlayer == arrayMaquina) { // Gana
            // Se incrementa en uno la dificultad
            contadorDificultad += 1
            // Limpia el arraylist del jugador
            arrayPlayer.clear()
            Toast.makeText(this@MainActivity, "Has ganado", Toast.LENGTH_LONG).show()
        } else { // Pierde
            enableButtons()
            // Limpia los arraylist del jugador y de la maquina
            arrayPlayer.clear()
            arrayMaquina.clear()
            // El contador de dificultad vuelve a 1
            contadorDificultad = 1
            Toast.makeText(this@MainActivity, "Has perdido", Toast.LENGTH_LONG).show()
        }
        // Display de la puntuacion del jugador
        var displayScore = contadorDificultad - 1
        score.text = displayScore.toString()

    }

    // OnClick boton jugador. Rellena los arraylist y realiza los cambios de estado de los botones
    fun jugadaMaquina(view: View) {
        enableButtons()
        if (contadorDificultad == 1) {
            for (c: Int in 0..contadorDificultad) {
                arrayMaquina.add(randomString())
            }
        } else {
            arrayMaquina.add(randomString())
        }
        val mpSimon = MediaPlayer.create(this, R.raw.simon_dice_sound)
        mpSimon.start()
        val handler = Handler()
        handler.postDelayed({
            var contador: Long = 0
            for (s: String in arrayMaquina) {
                showColor(s, contador)
                contador += 1
            }
        }, 1200)
        mpSimon.stop()
    }

    // Habilita los botones
    fun enableButtons() {
        Blue.isEnabled = true
        Red.isEnabled = true
        Yellow.isEnabled = true
        Green.isEnabled = true
    }

    // Deshabilita los botones
    fun disableButtons() {
        Blue.isEnabled = false
        Red.isEnabled = false
        Yellow.isEnabled = false
        Green.isEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Nos permite usar el id_nombre de la vista directamente sin usar R.id....
        setContentView(R.layout.activity_main)
        disableButtons()

    }
}
