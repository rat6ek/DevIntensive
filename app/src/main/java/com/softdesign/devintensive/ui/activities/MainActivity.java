package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.ImageCropper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private DataManager mDataManager;

    private List<EditText> mUserInfo;

    private int mCurrentEditMode = 0;

    /**
     * Вызывается при ( создании Activity | изменения конфигураций | возврата к текущей Activity после его уничтожения )
     * <p/>
     * В данном методе инициализируется/производится:
     *  - UI ( статика ),
     *  - Статические данные Activity,
     *  - Адаптеры ( связь данных со списками ),
     *<p/>
     * Не запускать длительные операций в данном методе
     * @param savedInstanceState - объект со значениями сохраненными в Bundle - состояние UI
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");


        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        EditText userPhone = (EditText) findViewById(R.id.phone_et);
        EditText userMail = (EditText) findViewById(R.id.email_et);
        EditText userVk = (EditText) findViewById(R.id.vk_profile_et);
        EditText userGit = (EditText) findViewById(R.id.github_et);
        EditText userBio = (EditText) findViewById(R.id.about_et);
        mDataManager = DataManager.getInstance();

        mUserInfo = new ArrayList<>();
        mUserInfo.add(userPhone);
        mUserInfo.add(userMail);
        mUserInfo.add(userVk);
        mUserInfo.add(userGit);
        mUserInfo.add(userBio);

        setupToolbar();
        setupDrawer();

        mFab.setOnClickListener(this);

        if (savedInstanceState == null) {
            // активити запускается впервые
            Log.d(TAG, "Activity запускается впервые");
        } else {
            //активити уже запускалось
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
        loadUserInfoValue();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Вызывается при старте Activity перед тем как UI станет доступен пользователю.
     * Как правило в данном методе происходит регистрация подписки на события, остановка которых
     * была совершена в методе onStop
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    /**
     * Вызывается, когда UI становится доступен пользователю для взаимодействия.
     * В данном методе происходит запуск анимаций / аудио / видео / запуск BroadcastReceiver
     * необходимых для реализаций UI логики / запуск потоков и.т.д.
     * Должен быть максимально легковесным для максимальной отзывчивости UI
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    /**
     * Вызывается, когда текущая Activity теряет фокус, но остаётся видимой
     * ( всплытие диалогового окна / частичное перекрытие другой Activity / и.т.д).
     * <p/>
     * В данном методе происходит сохранение легковесных UI данных / анимаций / видео / аудио и.т.д
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

        loadUserInfoValue();
    }

    /**
     * Вызывается, когда Activity становится невидимым для пользователя.
     * В данном методе происходит отписка от событий, остоновка сложных анимаций, сложные операции
     * по сохранению данных/прерывание запущенных потоков и.т.д
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    /**
     * Вызывается при рестарте Activity / возобновлении работы после вызова метода onStop.
     * В данном методе реализуется специфическая бизнес - логика, которая должна сработать
     * именно при рестарте Activity. Например : запрос к серверу, который необходимо совершать
     * при возвращении из другого Activity  ( обновление данных, подписка на определенное
     * событие проинициализированное на другом Activity / бизнес - логика завязанная именно на
     * рестарте Activity
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    /**
     * Вызывается при окончании работы Activity ( когда это происходит системно или после вызова
     * метода finish() )
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * Обрабатывает click'и по экрану устройства
     * @param v - view которое click'нули
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    /**
     * Обрабатывает нажатия кнопки системной кнопки Back
     */
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    /**
     * Показывает Snackbar с переданным ей message
     * @param message - текст который будет показан в Snackbar'е
     */
    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Инициализирует Toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Инициализирует NavigationDrawer
     */
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        assert navigationView != null;
        View drawerHeader = navigationView.getHeaderView(0);
        assert drawerHeader != null;
        ImageView avatar = (ImageView) drawerHeader.findViewById(R.id.drawer_header_avatar);
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.user_avatar);
        ImageCropper headerAvatar = new ImageCropper(img);
        avatar.setImageDrawable(headerAvatar);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    /**
     * Обрабатывает результаты работы других Activity запущенных через startActivityForResult
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Переключает режимы редактирования и чтения
     * @param mode 1 - режим редактирования, 0 - режим чтения
     */
    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
            }
            saveUserInfoValue();
        }
    }

    /**
     * Загружает данные пользователя из SharedPreferences
     */
    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfo.get(i).setText(userData.get(i));
        }
    }

    /**
     * Сохраняет введенные данные пользователя в SharedPreferences
     */
    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfo) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    /**
     * Загружает фото из галлереи
     */
    private void loadPhotoFromGallery() {

    }

    /**
     * Загружает фото из камеры
     */
    private void loadPhotoFromCamera() {

    }

}
