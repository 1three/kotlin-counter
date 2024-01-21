package fastcampus.part1.counter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 계수기(숫자 세기) 앱
 *
 * 1. + 버튼 클릭 시, 숫자 1 증가
 * 2. 초기화 버튼 클릭 시, 숫자 0 초기화
 * */

/**
 * 1. 화면 방향이 바뀌더라도 값을 유지하려면?
 * 1) onSaveInstanceState, onRestoreInstanceState
 *
 * 2. 화면 방향에 무관하게 늘 버튼이 보이도록 하려면?
 * 1) ScrollView
 * 2) numberTextView의 height 지정하지 않고 weight 이용
 *
 * 3. 왜 xml 파일의 weight에 0dp를 넣을까?
 * 1) LinearLayout weight 값을 적용하기 위함
 *    orientation=vertical → layout_height=0dp
 *    orientation=horizontal → layout_width=0dp
 * */

//class MainViewModel: ViewModel() {
//    var number = 0
//}

class MainActivity : AppCompatActivity() {
//    private lateinit var viewModel: MainViewModel
    private var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val numberTextView = findViewById<TextView>(R.id.numberTextView)
        val initBtn = findViewById<Button>(R.id.initBtn)
        val plusBtn = findViewById<Button>(R.id.plusBtn)

//        numberTextView.text = viewModel.number.toString()

        if (savedInstanceState != null) {
            number = savedInstanceState.getInt("number")
            numberTextView.text = number.toString()
        }

        initBtn.setOnClickListener {
//            viewModel.number = 0
//            numberTextView.text = viewModel.number.toString()
//            Log.d("Main - onClick", "init ${viewModel.number}")

            number = 0
            numberTextView.text = number.toString()
            Log.d("Main - onClick", "init $number")
        }
        plusBtn.setOnClickListener {
//            viewModel.number++
//            numberTextView.text = viewModel.number.toString()
//            Log.d("Main - onClick", "plus ${viewModel.number}")

            number++
            numberTextView.text = number.toString()
            Log.d("Main - onClick", "plus $number")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("number", number)
    }
}
