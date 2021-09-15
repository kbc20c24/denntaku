package com.example.calculator_int

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    /** 値を入れるリスト */
    private val valueList = mutableListOf<Int>()

    /** 一時的に値を入れておく変数 */
    private var value = 0

    /** 演算子を入れるリスト */
    private val operatorList = mutableListOf<Char>()

    /** 演算子ボタンが押されたか把握するための変数
     *  押されていない時：false
     *  押されている時：true */
    private var flagOpePressed = false

    /** 実行ボタンが押されたか把握するための変数
     *  押されていない時：false
     *  押されている時：true */
    private var flagEqualPressed = false

    /** 表示テキスト */
//    private val textArea = findViewById<TextView>(R.id.Output)
    //フィールドにfindViewById書いていると動かない！！

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /** ボタンが押された時に、決められたメソッドを呼ぶメソッド */
    fun buttonPress(v: View) {
        when (v.id) {
            R.id.btn0 -> numButtonAction(0)
            R.id.btn1 -> numButtonAction(1)
            R.id.btn2 -> numButtonAction(2)
            R.id.btn3 -> numButtonAction(3)
            R.id.btn4 -> numButtonAction(4)
            R.id.btn5 -> numButtonAction(5)
            R.id.btn6 -> numButtonAction(6)
            R.id.btn7 -> numButtonAction(7)
            R.id.btn8 -> numButtonAction(8)
            R.id.btn9 -> numButtonAction(9)
            R.id.btn_allClear -> allClearButtonAction()
            R.id.btn_changeOperator -> changeOperatorButtonAction()
            R.id.btn_plus -> calcButtonAction('+')
            R.id.btn_minus -> calcButtonAction('-')
            R.id.btn_time -> calcButtonAction('×')
            R.id.btn_divide -> calcButtonAction('÷')
            R.id.btn_equal -> equalButtonAction()
        }
    }

    /** ダイアログによるエラー処理
     *  引数の指定
     *  《8桁オーバー → 1》
     *  《0除算エラー(infinity) → 2》
     *  */
    private fun dialogErrorAction(errorTitleNum: Int) {
        val dialog = AlertDialog.Builder(this)
        when (errorTitleNum) {
            1 -> dialog.setTitle(R.string.dialog_FlowErrorTitle)
            2 -> dialog.setTitle(R.string.dialog_ArithmeticErrorTitle)
        }
        dialog
            .setMessage(R.string.dialog_message)
            .setPositiveButton(R.string.dialog_ok) { _, _ ->
                allClearButtonAction()
            }
            .show()
    }

    /** オールクリアボタンが押された時の処理をまとめた関数 */
    private fun allClearButtonAction() {
        value = 0
        valueList.clear()
        operatorList.clear()
        flagOpePressed = false
        flagEqualPressed = false
        findViewById<TextView>(R.id.Output).text = value.toString()
//        textArea.text = value.toString()
    }

    /** 数字ボタンが押された時の処理をまとめた関数 */
    private fun numButtonAction(num: Int) {
        if (!flagEqualPressed && !flagOpePressed && findViewById<TextView>(R.id.Output).text.toString().length >= 8) {
            dialogErrorAction(1)
        } else {
            flagOpePressed = false
            flagEqualPressed = false

            if (value == 0) {
                //まだ何も値が入力されていない場合
                value = num
                findViewById<TextView>(R.id.Output).text = value.toString()
            } else {
                //すでに値が入力されていた場合
                value = (value.toString() + num.toString()).toInt()
                findViewById<TextView>(R.id.Output).text = value.toString()
            }
        }
    }

    /** 演算子ボタンが押された時の処理をまとめた関数 */
    private fun calcButtonAction(op: Char) {
        if (!flagOpePressed) {
            operatorList.add(op)
            if (flagEqualPressed) {
                // 実行ボタンが押された後、演算子ボタンが押された場合
                valueList.add(findViewById<TextView>(R.id.Output).text.toString().toInt())
            } else {
                // 数字ボタンが押された後、演算子ボタンが押された場合
                // ここまでの計算結果を入れないといけない！！！
                calculation()
                findViewById<TextView>(R.id.Output).text = value.toString()
            }
            value = 0
            flagOpePressed = true
        } else {
            if (valueList.isNotEmpty()) {
                // 演算子ボタンが2回連続で押された場合
                operatorList.removeAt(operatorList.size - 1)
                operatorList.add(op)
            }
        }
    }

    /** 計算処理をまとめた関数 */
    private fun calculation() {
        try {
            valueList.add(value)
            value = valueList[0]
            for (i in 0 until valueList.size - 1) {
                when {
                    operatorList[i] == '+' -> value += valueList[i + 1]
                    operatorList[i] == '-' -> value -= valueList[i + 1]
                    operatorList[i] == '×' -> value *= valueList[i + 1]
                    operatorList[i] == '÷' -> value /= valueList[i + 1]
                }
            }
        } catch (e: ArithmeticException) {
            dialogErrorAction(2)
        }
    }

    /** 実行ボタン"="が押された時の処理 */
    private fun equalButtonAction() {
        if (valueList.size > 0 && operatorList.size > 0 && !flagOpePressed) calculation()
        if (value.toString().length > 8) {
            dialogErrorAction(1)
        } else {
            findViewById<TextView>(R.id.Output).text = value.toString()
            value = 0
            valueList.clear()
            operatorList.clear()
            flagEqualPressed = true
            flagOpePressed = false
        }
    }

    /** プラスマイナスボタンが押された時の処理 */
    @SuppressLint("CutPasteId")
    private fun changeOperatorButtonAction() {
        if (!flagOpePressed) {
            value = findViewById<TextView>(R.id.Output).text.toString().toInt() * (-1)
            findViewById<TextView>(R.id.Output).text = value.toString()
        }
    }

}