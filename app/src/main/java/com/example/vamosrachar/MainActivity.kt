package com.example.vamosrachar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    private var tts: TextToSpeech? = null

    var valor = 0.0;
    var numpessoas:Int = 0;
    var total:Double=0.0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val shareBtn = findViewById<FloatingActionButton>(R.id.share);
        val listenBtn = findViewById<FloatingActionButton>(R.id.listen);
        val preco = findViewById<EditText>(R.id.preco)
        val nPessoasField = findViewById<EditText>(R.id.pessoas)
        val totalPorPessoa = findViewById<TextView>(R.id.total);


        fun calculate() {
            if (numpessoas > 0) {
                total = valor / numpessoas;
                totalPorPessoa.text = String.format("%.${2}f", total).format(".",",")
            } else {
                totalPorPessoa.text = getString(R.string.bigger_than_0);
            }
        }
        shareBtn.setOnClickListener{
           val text= getString(R.string.your_paid)+String.format("%.${2}f", total).format(".",",")
            Log.e("Rachar",text)
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,text )
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, getString(R.string.app_name))
            startActivity(shareIntent)
        }
        val textToSpeech=TextToSpeech(this) {
            if (it != TextToSpeech.ERROR) {
                Log.e("Rachar","ERRO TTS")
            }
        };
        textToSpeech.language=Locale.getDefault();
        listenBtn.setOnClickListener{
            //val fal=String.format("%.${2}f", total).format(".",",")
            val cents:Int = floor((total-Math.floor(total))*10).toInt()
            val reais:Int = floor(total).toInt()
            val out="O valor a ser pago por cada um das $numpessoas pessoas são $reais reais e $cents centavos"
            textToSpeech.speak(
                out,
                TextToSpeech.QUEUE_FLUSH,null,null);
        }

        preco.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                try {
                    valor = s.toString().toDouble()
                    calculate()
                } catch (e: java.lang.NumberFormatException) {
                    // handler
                    totalPorPessoa.text="Insira um valor válido"
                    Log.e("PDM23", "Formato invalido");
                }
            }
        })
        nPessoasField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                try {
                    numpessoas = s.toString().toInt()
                    calculate();
                } catch (e: java.lang.NumberFormatException) {
                    // handler
                    totalPorPessoa.text=getString(R.string.insert_valid)
                    Log.e("PDM23", "Formato invalido");
                }
            }
        })
    }
}