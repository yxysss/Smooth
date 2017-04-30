# Smooth

Android文件下载器，支持断点续传，支持多线程下载。

使用前，把java文件夹下的library.smooth包拷贝到项目中的java文件夹下即可使用，上面给出的小demo实现了下载apk后自动更新安装。

使用方法:

调用SmoothLoader.getSmoothLoader(this)方法获取SmoothLoader实例。注意，一个应用只会获得一个SmoothLoader实例，在service,activity或者application的onCreate方法中注册，在onDestroy方法中调用SmoothLoader.cancel方法取消注册。

调用SmoothLoader.getSmoothLoad(this).addDownloadTask(String url, onStartListener onstartListener, onErrorListener onerrorListener, onFinishListener onfinishListener)方法添加下载地址，下载任务开始前的操作，下载任务出错的操作，下载任务结束的操作。

onerrorListener.onError(int Errorcode) 参数Errorcode = 0, 代表下载中断， Errorcode = 1, 代表下载正在进行中, Errorcode = 2, 代表下载任务的url无效。

简单示例：

smoothLoader = SmoothLoader.getSmoothLoader(this);

smoothLoader.addDownloadTask("http://192.168.1.102/QQ_500.apk", new onStartListener() {

                @Override
                public void onStart() {
                    Toast.makeText(MainActivity.this, "1号任务下载开始", Toast.LENGTH_LONG).show();
                }
            }, new onErrorListener() {
                @Override
                public void onError() {
                    if (Errorcode == 0) Toast.makeText(DownloadService.this, "1号任务下载失败", Toast.LENGTH_LONG).show();
                    if (Errorcode == 1) Toast.makeText(DownloadService.this, "1号任务正在下载", Toast.LENGTH_LONG).show();
                    if (Errorcode == 2) Toast.makeText(DownloadService.this, "1号任务的URL无效", Toast.LENGTH_LONG).show();
                }
            }, new onFinishedListener() {
                @Override
                public void onFinished() {
                    Toast.makeText(MainActivity.this, "1号任务下载完成", Toast.LENGTH_LONG).show();
                }
            });
