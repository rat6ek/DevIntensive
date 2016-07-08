package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.ImageCropper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";
    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private RelativeLayout mProfilePlaceholder;
    private DataManager mDataManager;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private AppBarLayout.LayoutParams mAppBarParams = null;
    private ImageView mProfileImage;

    EditText userPhone;
    EditText userMail;
    EditText userVk;
    EditText userGit;
    EditText userBio;

    private List<EditText> mUserInfo;

    private int mCurrentEditMode = 0;

    private File mPhotoFile = null;
    private Uri mSelectedImage = null;


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
        mProfilePlaceholder = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);

        userPhone = (EditText) findViewById(R.id.phone_et);
        userMail = (EditText) findViewById(R.id.email_et);
        userVk = (EditText) findViewById(R.id.vk_profile_et);
        userGit = (EditText) findViewById(R.id.github_et);
        userBio = (EditText) findViewById(R.id.about_et);
        mDataManager = DataManager.getInstance();

        mUserInfo = new ArrayList<>();
        mUserInfo.add(userPhone);
        mUserInfo.add(userMail);
        mUserInfo.add(userVk);
        mUserInfo.add(userGit);
        mUserInfo.add(userBio);

        ImageView call_icon = (ImageView) findViewById(R.id.icon_call);
        ImageView sendto_icon = (ImageView) findViewById(R.id.icon_sendto);
        ImageView vk_icon = (ImageView) findViewById(R.id.icon_vk_view);
        ImageView github_icon = (ImageView) findViewById(R.id.icon_github_view);

        setupToolbar();
        setupDrawer();
        loadUserInfoValue();
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.user_photo)
                .into(mProfileImage);


        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);

        call_icon.setOnClickListener(this);
        sendto_icon.setOnClickListener(this);
        vk_icon.setOnClickListener(this);
        github_icon.setOnClickListener(this);

        if (savedInstanceState == null) {
            // активити запускается впервые
            Log.d(TAG, "Activity запускается впервые");
        } else {
            //активити уже запускалось
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
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
            case R.id.profile_placeholder:
                //Действия при нажатии на relative layout 'profile_placeholder'
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.icon_call:
                call();
                break;
            case R.id.icon_sendto:
                sendEmail();
                break;
            case R.id.icon_vk_view:
                Intent openVk = new Intent(Intent.ACTION_VIEW);
                openVk.setData(Uri.parse("http://" + userVk.getText().toString()));
                startActivity(openVk);
                break;
            case R.id.icon_github_view:
                Intent openGit = new Intent(Intent.ACTION_VIEW);
                openGit.setData(Uri.parse("http://" + userGit.getText().toString()));
                startActivity(openGit);
                break;
        }
    }

    private void sendEmail() {
        Intent sendtoIntent = new Intent(Intent.ACTION_SEND);
        sendtoIntent.setType("text/plain");
        sendtoIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { userMail.getText().toString() });

        startActivity(Intent.createChooser(sendtoIntent, "Отправить Email"));
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

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();

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
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);

                    insertProfileImage(mSelectedImage);
                }
        }
    }

    /**
     * Устанавливает переданный в аргументы фото в Profile Image
     * @param selectedImage
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);
        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    /**
     * Переключает режимы редактирования и чтения
     * @param mode 1 - режим редактирования, 0 - режим чтения
     */
    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);

            }
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            hideProfilePlaceholder();
            unlockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
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
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_choose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    /**
     * Загружает фото из камеры
     */
    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }

        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout, "Для корректной работы необходимо дать разрешения", Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openApplicationSettings();
                        }
                    }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    /**
     * Скрывает profile placeholder для смены фото профиля
     */
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    /**
     * Показывает profile placeholder для смены фото профиля
     */
    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    /**
     * Блокирует сворачивание верхнего Toolbar
     */
    private void lockToolbar() {
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    /**
     * Разблокировывает сворачивание верхнего Toolbar
     */
    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch (choiceItem) {

                            case 0:
                                // Загрузить из галлереи
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                // Сделать снимок
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                // Отмена
                                dialog.cancel();
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    /**
     * Создает пустой фотофайл
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    /**
     * Открывает окно настроек в случае отсутсвия разрешения на определенное действие
     */
    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    private void call() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + userPhone.getText().toString()));
        startActivity(callIntent);
    }

}
