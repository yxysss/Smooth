# Smooth
Android文件下载器，支持断点续传。
使用前，将文件包放在Android项目的library.smooth包下即可。
使用方法:
调用SmoothLoader.getSmoothLoader(this)方法获取SmoothLoader实例。
调用SmoothLoader.getSmoothLoad(this).addDownloadTask(String url, onStartListener onstartListener, onErrorListener onerrorListener, onFinishListener onfinishListener)方法添加下载地址，下载任务开始前的操作，下载任务出错的操作，下载任务结束的操作。
调用SmoothLoader.getSmoothLoad(this).getCacheDir()方法获取默认的下载目录。
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
smoothLoader.addDownloadTask("http://192.168.1.102/QQ.apk", new onStartListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(MainActivity.this, "2号任务下载开始", Toast.LENGTH_LONG).show();
                    }
                }, new onErrorListener() {
                    @Override
                    public void onError() {
                        Toast.makeText(MainActivity.this, "2号任务下载失败", Toast.LENGTH_LONG).show();
                    }
                }, new onFinishedListener() {
                    @Override
                    public void onFinished() {
                        Toast.makeText(MainActivity.this, "2号任务下载完成", Toast.LENGTH_LONG).show();
                    }
                });
smoothLoader.getCacheDir();
下载文件以"前缀+源文件名"的形式命名保存在下载目录中。
