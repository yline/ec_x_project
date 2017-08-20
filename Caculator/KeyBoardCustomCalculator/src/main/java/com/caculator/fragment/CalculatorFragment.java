package com.caculator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.caculator.R;
import com.yline.base.BaseFragment;
import com.yline.log.LogFileUtil;

/**
 * Created by yline on 2017/1/7.
 */
public class CalculatorFragment extends BaseFragment implements View.OnClickListener
{
	private static final String SYMBOL_0 = "0";

	private static final String SYMBOL_1 = "1";

	private static final String SYMBOL_2 = "2";

	private static final String SYMBOL_3 = "3";

	private static final String SYMBOL_4 = "4";

	private static final String SYMBOL_5 = "5";

	private static final String SYMBOL_6 = "6";

	private static final String SYMBOL_7 = "7";

	private static final String SYMBOL_8 = "8";

	private static final String SYMBOL_9 = "9";

	public static final String SYMBOL_ADD = "+";

	public static final String SYMBOL_REDUCE = "-";

	public static final String SYMBOL_MULTIPLY = "×";

	public static final String SYMBOL_DIVIDE = "÷";

	public static final String SYMBOL_SQUARE = "^(2)";

	public static final String SYMBOL_RADICALS = "^(1/2)";

	private static final String SYMBOL_POINT_SHOW = "•";

	public static final String SYMBOL_POINT_USE = ".";

	private static final String SYMBOL_EQUAL = "=";

	private static final String SYMBOL_CLEAR = "C";

	private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnPoint;

	private Button btnAdd, btnReduce, btnMultiply, btnDivide, btnSquare, btnRadicals;

	private Button btnEqual, btnClear;

	private TextView tvShow;

	private StringBuffer firstNumber, secondNumber;

	private String symbol;

	@Nullable
	@Override

	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_calculator, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		initData(view);
	}

	private void initView(View view)
	{
		// 数字
		btn0 = (Button) view.findViewById(R.id.btn_0);
		btn1 = (Button) view.findViewById(R.id.btn_1);
		btn2 = (Button) view.findViewById(R.id.btn_2);
		btn3 = (Button) view.findViewById(R.id.btn_3);
		btn4 = (Button) view.findViewById(R.id.btn_4);
		btn5 = (Button) view.findViewById(R.id.btn_5);
		btn6 = (Button) view.findViewById(R.id.btn_6);
		btn7 = (Button) view.findViewById(R.id.btn_7);
		btn8 = (Button) view.findViewById(R.id.btn_8);
		btn9 = (Button) view.findViewById(R.id.btn_9);
		btnPoint = (Button) view.findViewById(R.id.btn_point);
		btn0.setText(SYMBOL_0);
		btn1.setText(SYMBOL_1);
		btn2.setText(SYMBOL_2);
		btn3.setText(SYMBOL_3);
		btn4.setText(SYMBOL_4);
		btn5.setText(SYMBOL_5);
		btn6.setText(SYMBOL_6);
		btn7.setText(SYMBOL_7);
		btn8.setText(SYMBOL_8);
		btn9.setText(SYMBOL_9);
		btnPoint.setText(SYMBOL_POINT_SHOW);

		// 符号
		btnAdd = (Button) view.findViewById(R.id.btn_add);
		btnReduce = (Button) view.findViewById(R.id.btn_reduce);
		btnMultiply = (Button) view.findViewById(R.id.btn_multiply);
		btnDivide = (Button) view.findViewById(R.id.btn_divide);
		btnSquare = (Button) view.findViewById(R.id.btn_square);
		btnRadicals = (Button) view.findViewById(R.id.btn_radicals);
		btnAdd.setText(SYMBOL_ADD);
		btnReduce.setText(SYMBOL_REDUCE);
		btnMultiply.setText(SYMBOL_MULTIPLY);
		btnDivide.setText(SYMBOL_DIVIDE);
		btnSquare.setText(SYMBOL_SQUARE);
		btnRadicals.setText(SYMBOL_RADICALS);

		// 其它
		btnEqual = (Button) view.findViewById(R.id.btn_equal);
		btnClear = (Button) view.findViewById(R.id.btn_clear);
		btnEqual.setText(SYMBOL_EQUAL);
		btnClear.setText(SYMBOL_CLEAR);

		tvShow = (TextView) view.findViewById(R.id.tv_show);
	}

	private void initData(View view)
	{
		btn0.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);
		btnPoint.setOnClickListener(this);

		btnAdd.setOnClickListener(this);
		btnReduce.setOnClickListener(this);
		btnMultiply.setOnClickListener(this);
		btnDivide.setOnClickListener(this);
		btnSquare.setOnClickListener(this);
		btnRadicals.setOnClickListener(this);

		btnEqual.setOnClickListener(this);
		btnClear.setOnClickListener(this);

		clearState();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_0:
				updateState(SYMBOL_0);
				break;
			case R.id.btn_1:
				updateState(SYMBOL_1);
				break;
			case R.id.btn_2:
				updateState(SYMBOL_2);
				break;
			case R.id.btn_3:
				updateState(SYMBOL_3);
				break;
			case R.id.btn_4:
				updateState(SYMBOL_4);
				break;
			case R.id.btn_5:
				updateState(SYMBOL_5);
				break;
			case R.id.btn_6:
				updateState(SYMBOL_6);
				break;
			case R.id.btn_7:
				updateState(SYMBOL_7);
				break;
			case R.id.btn_8:
				updateState(SYMBOL_8);
				break;
			case R.id.btn_9:
				updateState(SYMBOL_9);
				break;
			case R.id.btn_point:
				updateState(SYMBOL_POINT_USE);
				break;
			case R.id.btn_add:
				updateState(SYMBOL_ADD);
				break;
			case R.id.btn_reduce:
				updateState(SYMBOL_REDUCE);
				break;
			case R.id.btn_multiply:
				updateState(SYMBOL_MULTIPLY);
				break;
			case R.id.btn_divide:
				updateState(SYMBOL_DIVIDE);
				break;
			case R.id.btn_square:
				updateState(SYMBOL_SQUARE);
				break;
			case R.id.btn_radicals:
				updateState(SYMBOL_RADICALS);
				break;
			case R.id.btn_equal:
				resetState();
				break;
			case R.id.btn_clear:
				clearState();
				break;
		}
	}

	/**
	 * 如果开始输入 +*等符号,则不显示
	 * 如果没有输入过符号,则不输入第二个字符
	 * @param value
	 */
	private void updateState(String value)
	{
		LogFileUtil.v("updateState value = " + value);
		switch (value)
		{
			case SYMBOL_0:
			case SYMBOL_1:
			case SYMBOL_2:
			case SYMBOL_3:
			case SYMBOL_4:
			case SYMBOL_5:
			case SYMBOL_6:
			case SYMBOL_7:
			case SYMBOL_8:
			case SYMBOL_9:
				if (TextUtils.isEmpty(symbol))
				{
					// 如果开头是 0 或 -0 则将0去掉
					if (SYMBOL_0.equals(firstNumber.toString()) || (SYMBOL_REDUCE + SYMBOL_0).equals(firstNumber.toString()))
					{
						firstNumber.deleteCharAt(firstNumber.length() - 1);
					}
					firstNumber.append(value);
				}
				else if (symbol.equals(SYMBOL_ADD) || symbol.equals(SYMBOL_REDUCE) || symbol.equals(SYMBOL_MULTIPLY) || symbol.equals(SYMBOL_DIVIDE))
				{
					// 如果开头是 0 或 -0 则将0去掉
					if (SYMBOL_0.equals(secondNumber.toString()) || (SYMBOL_REDUCE + SYMBOL_0).equals(secondNumber.toString()))
					{
						secondNumber.deleteCharAt(secondNumber.length() - 1);
					}
					secondNumber.append(value);
				}
				break;
			case SYMBOL_POINT_USE:
				if (TextUtils.isEmpty(symbol))
				{
					// 如果包含 "."则不再添加"."
					if (!firstNumber.toString().contains(SYMBOL_POINT_USE))
					{
						firstNumber.append(value);
					}
				}
				else if (symbol.equals(SYMBOL_ADD) || symbol.equals(SYMBOL_REDUCE) || symbol.equals(SYMBOL_MULTIPLY) || symbol.equals(SYMBOL_DIVIDE))
				{
					// 如果包含 "."则不再添加"."
					if (!secondNumber.toString().contains(SYMBOL_POINT_USE))
					{
						secondNumber.append(value);
					}
				}
				break;
			case SYMBOL_REDUCE:
				if (TextUtils.isEmpty(firstNumber))
				{
					firstNumber.append(value);
				}
				else
				{
					symbol = value;
				}
				break;
			case SYMBOL_ADD:
			case SYMBOL_MULTIPLY:
			case SYMBOL_DIVIDE:
			case SYMBOL_SQUARE:
			case SYMBOL_RADICALS:
				if (!TextUtils.isEmpty(firstNumber) && !SYMBOL_REDUCE.equals(firstNumber.toString()))
				{
					symbol = value;
				}
				break;
		}

		// 更新Ui
		tvShow.setText(firstNumber.toString() + symbol + secondNumber.toString());
	}

	/**
	 * 按下 C 符号
	 */
	private void clearState()
	{
		// 更新显示内容
		firstNumber = new StringBuffer();
		secondNumber = new StringBuffer();
		symbol = "";

		// 更新Ui
		tvShow.setText(firstNumber.toString() + symbol + secondNumber.toString());
	}

	/**
	 * 按下 等号
	 */
	private void resetState()
	{
		if (getActivity() instanceof ICalculateCallback)
		{
			String tempResult = "";
			switch (symbol)
			{
				case SYMBOL_ADD:
				case SYMBOL_REDUCE:
				case SYMBOL_MULTIPLY:
				case SYMBOL_DIVIDE:
					tempResult = ((ICalculateCallback) getActivity()).onResult(firstNumber.toString(), symbol, secondNumber.toString());
					updateUIResetState(tempResult);
					break;
				case SYMBOL_SQUARE:
				case SYMBOL_RADICALS:
					tempResult = ((ICalculateCallback) getActivity()).onResult(firstNumber.toString(), symbol);
					updateUIResetState(tempResult);
					break;
			}
		}
	}

	private void updateUIResetState(String tempResult)
	{
		// 更新显示内容
		symbol = "";
		secondNumber = new StringBuffer();
		if (TextUtils.isEmpty(tempResult))
		{
			firstNumber = new StringBuffer();
		}
		else
		{
			firstNumber = new StringBuffer(tempResult);
		}

		// 更新Ui
		tvShow.setText(firstNumber.toString() + symbol + secondNumber.toString());
	}

	public interface ICalculateCallback
	{
		String onResult(String first, String symbol);

		String onResult(String first, String symbol, String second);
	}
}
