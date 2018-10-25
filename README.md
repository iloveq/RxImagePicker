
RxImagePicker

![takePhoto.gif](https://upload-images.jianshu.io/upload_images/8886407-d5ba5d648394eb06.gif?imageMogr2/auto-orient/strip)

![pickImage.gif](https://upload-images.jianshu.io/upload_images/8886407-8051a2779827a117.gif?imageMogr2/auto-orient/strip)

### 优点：
① rxjava2 实现读取相册和调用系统相机拍照，File - Uri - path 转换和线程切换
② 根据你设置的主题ui 图片选择界面和图片预览界面自适应
③ 配置简单，结果回调方便（不用写onActivityResult）兼容7.0 fileprovider不用自己配置
④ 加载图片的框架自由选择，Glide Pisco Freso 都可以
⑤ 回调结果可配置，File - Uri - path 需手动强转
### 使用：
① 引入
```
    //Add the JitPack repository to your build file
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
    //Add the dependency
    dependencies {
	        implementation 'com.github.woaigmz:RxImagePicker:0.0.4'
	}
```
② 使用：不提供权限检测 //根据设置的ResultType 在回调里手动转换类型
```
requestPermissions(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                RxImagePicker
                        .source(MainActivity.this, new ImageLoaderInterface<ImageView>() {
                            @Override
                            public void displayImage(Context context, String path, ImageView imageView, int width, int height, float scale) {
                                ImageLoader.loadImageWithSize(context, imageView, path, width, height, scale);
                            }
                        })
                        .createFactory()
                        .setGridColumn(3)
                        .setTheme(R.style.AppTheme)
                        .setPickedNum(9)
                        .setResultType(ResultType.PATH)
                        .onPicked(new ImagePickerResultListener() {
                            @Override
                            public void onPicked(Object o) {
                                List<String> list = (List<String>) o;
                                String path = list.get(0);
                                ImageLoader.loadImageWithPath(MainActivity.this, iv, path);
                            }

                            @Override
                            public void onPhotoTook(Object o) {
                                Log.d("111", o.toString());
                                String path = (String) o;
                                ImageLoader.loadImageWithPath(MainActivity.this, iv, path);
                            }

                            @Override
                            public void onException(String msg) {
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
            }

            @Override
            public void onDenied(List<String> list) {

            }
        });
```
### 设计：
![project.png](https://upload-images.jianshu.io/upload_images/8886407-2c8eceabde959d91.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

① UI ：图片选择网格页面和预览选择页面 ImageChooseGridActivity/PreviewChooseActivity 通过 -- ImageChooseUI这个类拿到配置信息
② Factory：ImagePickerFactory 通过建造者模式 初始化配置
③ ImageLoaderInterface ：对外提供图片引擎适配接口 适配器模式
④ controller：ImageController Rxjava 数据处理地方，提供方法，供外界调用
⑤ model：Counter 单例模式，存储所有 Image 计算选择的ImageList 类似于数据库
⑥ RxImagePicker 入口

##### 谢谢：）for the dream
