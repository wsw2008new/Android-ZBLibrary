/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.library.base;

import zuo.biao.library.R;
import zuo.biao.library.base.BaseBottomWindow;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.interfaces.ViewPresenter;
import zuo.biao.library.util.StringUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**基础带标签的FragmentActivity
 * @author Lemon
 * @see #onCreate
 * @see #setContentView
 * @use extends BaseViewBottomWindow, 具体参考.DemoTabActivity
 * @must 在子类onCreate中调用initView();initData();initListener();
 */
public abstract class BaseViewBottomWindow<T, BV extends BaseView<T>> extends BaseBottomWindow
implements OnClickListener, ViewPresenter {
//	private static final String TAG = "BaseViewBottomWindow";


	/**
	 * @param savedInstanceState
	 * @return
	 * @must 1.不要在子类重复这个类中onCreate中的代码;
	 *       2.在子类onCreate中super.onCreate(savedInstanceState);
	 *       initView();initData();initListener();
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		onCreate(savedInstanceState, 0);
	}
	/**
	 * @param savedInstanceState
	 * @param layoutResID activity全局视图view的布局资源id，默认值为R.layout.base_tab_activity
	 * @param listener this - 滑动返回 ; null - 没有滑动返回
	 * @return
	 * @must 1.不要在子类重复这个类中onCreate中的代码;
	 *       2.在子类onCreate中super.onCreate(savedInstanceState, layoutResID, listener);
	 *       initView();initData();initListener();
	 */
	protected final void onCreate(Bundle savedInstanceState, int layoutResID) {
		super.onCreate(savedInstanceState);
		super.setContentView(layoutResID <= 0 ? R.layout.base_view_bottom_window : layoutResID);
	}

	//	//重写setContentView后这个方法一定会被调用，final有无都会导致崩溃，去掉throw Exception也会导致contentView为null而崩溃
	//	//防止子类中setContentView <<<<<<<<<<<<<<<<<<<<<<<<
	//	/**
	//	 * @warn 不支持setContentView，传界面布局请使用onCreate(Bundle savedInstanceState, int layoutResID)等方法
	//	 */
	//	@Override
	//	public final void setContentView(int layoutResID) {
	//		setContentView(null);
	//	}
	//	/**
	//	 * @warn 不支持setContentView，传界面布局请使用onCreate(Bundle savedInstanceState, int layoutResID)等方法
	//	 */
	//	@Override
	//	public final void setContentView(View view) {
	//		setContentView(null, null);
	//	}
	//	/**
	//	 * @warn 不支持setContentView，传界面布局请使用onCreate(Bundle savedInstanceState, int layoutResID)等方法
	//	 */
	//	@Override
	//	public final void setContentView(View view, LayoutParams params) {
	//		throw new UnsupportedOperationException(TAG + "不支持setContentView" +
	//				"，传界面布局请使用onCreate(Bundle savedInstanceState, int layoutResID)等方法");
	//	}
	//	//防止子类中setContentView >>>>>>>>>>>>>>>>>>>>>>>>>



	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Nullable
	protected TextView tvBaseViewBottomWindowTitle;

	@Nullable
	protected ViewGroup llBaseViewBottomWindowContainer;

	@Nullable
	protected TextView tvBaseViewBottomWindowReturn;
	@Nullable
	protected TextView tvBaseViewBottomWindowForward;
	/**
	 * 如果在子类中调用(即super.initView());则view必须含有initView中初始化用到的id(非@Nullable标记)且id对应的View的类型全部相同；
	 * 否则必须在子类initView中重写这个类中initView内的代码(所有id替换成可用id)
	 */
	@Override
	public void initView() {// 必须调用
		super.initView();

		tvBaseViewBottomWindowTitle = (TextView) findViewById(R.id.tvBaseViewBottomWindowTitle);

		llBaseViewBottomWindowContainer = (ViewGroup) findViewById(R.id.llBaseViewBottomWindowContainer);

		tvBaseViewBottomWindowReturn = (TextView) findViewById(R.id.tvBaseViewBottomWindowReturn);
		tvBaseViewBottomWindowForward = (TextView) findViewById(R.id.tvBaseViewBottomWindowForward);
	}


	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	// data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	protected T data;
	protected BV containerView;
	@Override
	public void initData() {// 必须调用
		super.initData();

		if (tvBaseViewBottomWindowTitle != null) {
			String title = getIntent().getStringExtra(INTENT_TITLE);
			if (StringUtil.isNotEmpty(title, true) == false) {
				title = getTitleName();
			}
			tvBaseViewBottomWindowTitle.setVisibility(StringUtil.isNotEmpty(title, true) ? View.VISIBLE : View.GONE);
			tvBaseViewBottomWindowTitle.setText(StringUtil.getTrimedString(title));
		}

		if (tvBaseViewBottomWindowReturn != null) {
			if (StringUtil.isNotEmpty(getReturnName(), true)) {
				tvBaseViewBottomWindowReturn.setText(StringUtil.getCurrentString());
			}
		}
		if (tvBaseViewBottomWindowForward != null) {
			if (StringUtil.isNotEmpty(getForwardName(), true)) {
				tvBaseViewBottomWindowForward.setText(StringUtil.getCurrentString());
			}
		}

		llBaseViewBottomWindowContainer.removeAllViews();
		if (containerView == null) {
			containerView = createView();
			llBaseViewBottomWindowContainer.addView(containerView.createView(inflater));
		}
		containerView.setView(null);
	}

	/**
	 * 创建新的内容View
	 * @return
	 */
	@NonNull
	protected abstract BV createView();
	/**
	 * 设置需要返回的结果
	 */
	protected abstract void setResult();

	// data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	// listener事件监听区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initListener() {// 必须调用
		super.initListener();

		if (tvBaseViewBottomWindowReturn != null) {
			tvBaseViewBottomWindowReturn.setOnClickListener(this);
		}
		if (tvBaseViewBottomWindowForward != null) {
			tvBaseViewBottomWindowForward.setOnClickListener(this);
		}
	}

	// 系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvBaseViewBottomWindowReturn) {
			finish();
		} else if (v.getId() == R.id.tvBaseViewBottomWindowForward) {
			setResult();
			finish();
		}
	}


	// 类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	@Override
	protected void onDestroy() {
		data = null;
		if (llBaseViewBottomWindowContainer != null) {
			llBaseViewBottomWindowContainer.removeAllViews();
		}
		if (containerView != null) {
			containerView.onDestroy();
		}

		super.onDestroy();

		containerView = null;

		tvBaseViewBottomWindowTitle = null;

		llBaseViewBottomWindowContainer = null;

		tvBaseViewBottomWindowReturn = null;
		tvBaseViewBottomWindowForward = null;
	}

	// 类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	// listener事件监听区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	// 内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}