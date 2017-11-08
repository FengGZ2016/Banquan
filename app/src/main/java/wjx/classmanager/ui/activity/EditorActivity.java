package wjx.classmanager.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import wjx.classmanager.R;
import wjx.classmanager.listener.SoftkeyListener;

import static wjx.classmanager.model.Constant.MyClass.CLASS_EDIT;

public class EditorActivity extends BaseActivity implements View.OnClickListener,SoftkeyListener.OnSoftKeyBoardChangeListener{

    private FloatingActionButton mFloatingActionButton;
    private TextView mTextView;
    private EditText mEditText;

    @Override
    protected void init() {

    }

    @Override
    public void initView() {
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mTextView = (TextView) findViewById(R.id.edit_name);
        mEditText = (EditText) findViewById(R.id.edit_input);
    }

    @Override
    public void initListener() {
        mFloatingActionButton.setOnClickListener(this);
//        SoftkeyListener.setListener(this,this);
    }

    @Override
    public void initData() {
        mTextView.setText("编辑页");
    }

    @Override
    public int getLayout() {
        return R.layout.activity_editor;
    }

    @Override
    public boolean isImmersive() {
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(CLASS_EDIT,mEditText.getText().toString().trim());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void keyBoardShow(int height) {
        mFloatingActionButton.scrollBy(0, height);
    }

    @Override
    public void keyBoardHide(int height) {
        mFloatingActionButton.scrollBy(0, height);
    }
}
