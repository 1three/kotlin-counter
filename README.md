# 🧮 Counter

```
1. + 버튼을 클릭할 경우, 숫자 1이 증가합니다.
2. 초기화 버튼을 클릭할 경우, 숫자가 0으로 초기화됩니다.
```

<br>

## 📋 더 나아가기

```
1. 화면 방향이 바뀌면 (회전하면) 왜 초기화될까?
2. 화면 방향이 바뀌더라도 값을 유지하려면 어떻게 해야할까?
3. 화면 방향이 바뀌더라도 (화면 방향에 무관하게) 늘 버튼이 보이도록 하려면 어떻게 해야할까?
4. 왜 xml 파일의 weight에 0dp를 넣을까?
```

<br>

## 📎 화면 방향이 바뀌면 (회전하면) 왜 초기화될까?

안드로이드에서 화면 방향이 바뀔 때 (즉, 기기가 세로에서 가로로 또는 그 반대로 회전할 때)<br>
시스템은 _**현재의 액티비티를 파괴하고 재생성**_ 하게 됩니다.<br>
이는 화면 구성이 방향에 따라 달라질 수 있기 때문입니다.<br>
이 과정에서 액티비티의 모든 상태 정보가 초기화되는데, 그래서 화면 회전 시 데이터가 초기화되는 것입니다.<br>

<br>

## 📎 화면 방향이 바뀌더라도 값을 유지하려면 어떻게 해야할까?

_**(1) onSaveInstanceState()와 onRestoreInstanceState() 사용**_

```Kotlin
class MainActivity : AppCompatActivity() {
    private var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numberTextView = findViewById<TextView>(R.id.numberTextView)
        val initBtn = findViewById<Button>(R.id.initBtn)
        val plusBtn = findViewById<Button>(R.id.plusBtn)

        savedInstanceState?.let {
            number = it.getInt("number")
            numberTextView.text = number.toString()
        }

        initBtn.setOnClickListener {
            number = 0
            numberTextView.text = number.toString()
            Log.d("Main - onClick", "init $number")
        }
        plusBtn.setOnClickListener {
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
```

<br>

_**(2) ViewModel 사용**_

```Kotlin
class MainViewModel : ViewModel() {
    var number = 0
}

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numberTextView = findViewById<TextView>(R.id.numberTextView)
        val initBtn = findViewById<Button>(R.id.initBtn)
        val plusBtn = findViewById<Button>(R.id.plusBtn)

        numberTextView.text = viewModel.number.toString()

        initBtn.setOnClickListener {
            viewModel.number = 0
            numberTextView.text = viewModel.number.toString()
            Log.d("Main - onClick", "init ${viewModel.number}")
        }
        plusBtn.setOnClickListener {
            viewModel.number++
            numberTextView.text = viewModel.number.toString()
            Log.d("Main - onClick", "plus ${viewModel.number}")
        }
    }
}
```

<br>

_**(3) setRetainInstance(true) 사용**_

setRetainInstance(true)는 _**프래그먼트에서만 사용 가능한**_ 메소드로,<br>
프래그먼트 인스턴스가 재생성되지 않고 유지되도록 설정하는 메서드입니다.

```Kotlin
class CounterFragment : Fragment(R.layout.fragment_layout) {
    private var number = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRetainInstance(true) // 화면 방향이 바뀌어도 Fragment 인스턴스 재생성 X (유지)

        val numberTextView = view.findViewById<TextView>(R.id.numberTextView)
        val initBtn = view.findViewById<Button>(R.id.initBtn)
        val plusBtn = view.findViewById<Button>(R.id.plusBtn)

        numberTextView.text = number.toString()

        initBtn.setOnClickListener {
            number = 0
            numberTextView.text = number.toString()
        }
        plusBtn.setOnClickListener {
            number++
            numberTextView.text = number.toString()
        }
    }
}
```

<br>

## 📎 화면 방향이 바뀌더라도 늘 버튼이 보이도록 하려면 어떻게 해야할까?

_**(1) ScrollView 사용**_

화면이 회전하거나 크기가 변경되어서 버튼이 화면 밖으로 나가버리는 경우,<br>
ScrollView를 사용하여 화면을 스크롤할 수 있게 만들 수 있습니다.<br>
이 방법은 단순하고 효과적이며, 여러 개의 뷰가 모두 화면에 보일 수 있도록 합니다.

```xml
<ScrollView 
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout 
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <!-- ... -->

    </LinearLayout>

</ScrollView>
```

<br>

_**(2) 레이아웃 가중치(weight) 사용**_

LinearLayout에서는 layout_weight 속성을 사용하여 뷰 간의 공간 분배 비율을 지정할 수 있습니다.<br>
이를 통해 화면 크기와 방향에 관계없이 버튼이 항상 보이게 할 수 있습니다.

```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:gravity="center"
        android:orientation="horizontal">

        <!-- ... -->

    </LinearLayout>

</LinearLayout>
```

<br>

_**(3) ConstraintLayout 사용**_

ConstraintLayout은 화면 비율에 맞출 수 있는 유동적인 레이아웃이며,<br>
각 요소를 화면에 고정시키거나 서로 상대적으로 위치시키는 것이 가능합니다.<br>
이를 통해 화면 방향이 바뀔 때 버튼이 항상 보이도록 할 수 있습니다.

 <br>

## 📎 왜 xml 파일의 weight에 0dp를 넣을까?

 XML 파일에서 weight 속성을 사용할 때 0dp를 넣는 이유는<br>
 뷰의 너비나 높이를 뷰의 weight 값에 따라 동적으로 변경하려는 의도 때문입니다.<br>
 LinearLayout에서 weight 속성은 해당 뷰가 차지하는 공간의 비율을 지정합니다.<br>
 너비나 높이를 0dp로 설정하면, 해당 뷰는 크기를 weight 값에 따라 결정하게 됩니다.<br>
 따라서 화면 크기 및 방향에 관계 없이 동적으로 뷰의 크기를 조절할 수 있습니다.
