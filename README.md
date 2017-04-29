# Smooth

Android文件下载器，支持断点续传，支持多线程下载。

使用前，把java文件夹下的library.smooth包拷贝到项目中的java文件夹下即可使用，上面给出的小demo实现了下载apk后自动更新安装。

使用方法:

调用SmoothLoader.getSmoothLoader(this)方法获取SmoothLoader实例。

调用SmoothLoader.getSmoothLoad(this).addDownloadTask(String url, onStartListener onstartListener, onErrorListener 

onerrorListener, onFinishListener onfinishListener)方法添加下载地址，下载任务开始前的操作，下载任务出错的操作，下载任务结束的操作。

调用SmoothLoader.getSmoothLoad(this).getCacheDir()方法获取默认的下载目录。
调用SmoothLoader.getSmoothLoad(this).getCacheDir(String url)方法获取下载完成后的最终文件。
下载完成的文件以"over+前缀+源文件名"的形式命名保存在下载目录中。
未下载完成的文件的文件名前没有"over"。

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
                        Toast.makeText(MainActivity.this, "1号任务下载失败", Toast.LENGTH_LONG).show();
                    }
                }, new onFinishedListener() {
                    @Override
                    public void onFinished() {
                        Toast.makeText(MainActivity.this, "1号任务下载完成", Toast.LENGTH_LONG).show();
                    }
                });
