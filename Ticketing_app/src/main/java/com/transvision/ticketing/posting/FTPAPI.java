package com.transvision.ticketing.posting;

        import android.annotation.SuppressLint;
        import android.app.ProgressDialog;
        import android.os.AsyncTask;
        import android.os.Handler;

        import com.transvision.ticketing.extra.FunctionsCall;

        import org.apache.commons.net.ftp.FTP;
        import org.apache.commons.net.ftp.FTPClient;
        import org.apache.commons.net.ftp.FTPConnectionClosedException;
        import org.apache.commons.net.ftp.FTPFile;
        import org.apache.commons.net.ftp.FTPReply;

        import java.io.BufferedOutputStream;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;

        import static com.transvision.ticketing.extra.Constants.APK_FILE_DOWNLOADED;
        import static com.transvision.ticketing.extra.Constants.APK_FILE_NOT_FOUND;
        import static com.transvision.ticketing.extra.Constants.DOWNLOAD_FILE_ERROR;
        import static com.transvision.ticketing.extra.Constants.DOWNLOAD_FILE_FAILURE;
        import static com.transvision.ticketing.extra.Constants.DOWNLOAD_FILE_SUCCESS;
        import static com.transvision.ticketing.extra.Constants.FILE_UPLOADED;
        import static com.transvision.ticketing.extra.Constants.FILE_UPLOADED_ERROR;
        import static com.transvision.ticketing.extra.Constants.FTP_HOST;
        import static com.transvision.ticketing.extra.Constants.FTP_PASS;
        import static com.transvision.ticketing.extra.Constants.FTP_PORT;
        import static com.transvision.ticketing.extra.Constants.FTP_USER;

public class FTPAPI {
    private FunctionsCall functionsCall = new FunctionsCall();

    //    @SuppressLint("StaticFieldLeak")
//    public class Upload_file extends AsyncTask<String, String, String> {
//        FileInputStream fis = null;
//        boolean result = false;
//        Handler handler;
//        String mobilepath ="";
//
//        public Upload_file(Handler handler) {
//            this.handler = handler;
//        }
//        @Override
//        protected String doInBackground(String... params) {
//            mobilepath = params[0];
//            functionsCall.logStatus("Main_Upload_Images Images uploading started");
//            FTPClient client = new FTPClient();
//            functionsCall.logStatus("Main_Upload_Images 1");
//            try {
//                client.connect(FTP_HOST, FTP_PORT);
//                functionsCall.logStatus("Main_Upload_Images 2");
//                int reply_from_server = client.getReplyCode();
//                functionsCall.logStatus("Main_Upload_Images 3");
//                if (FTPReply.isPositiveCompletion(reply_from_server)) {
//                    functionsCall.logStatus("Main_Upload_Images 3_server");
//                }
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            try {
//                functionsCall.logStatus("Main_Upload_Images 4");
//                client.login(FTP_USER, FTP_PASS);
//                functionsCall.logStatus("Main_Upload_Images 5");
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            try {
//                functionsCall.logStatus("Main_Upload_Images 6");
//                client.setFileType(FTP.BINARY_FILE_TYPE);
//                functionsCall.logStatus("Main_Upload_Images 7");
//                client.enterLocalPassiveMode();
//                functionsCall.logStatus("Main_Upload_Images 8");
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            try {
//                functionsCall.logStatus("Main_Upload_Images 9");
//                client.changeWorkingDirectory("/Android/Ticketing/"); // storage path
//                functionsCall.logStatus("Main_Upload_Images 10");
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            try {
//                functionsCall.logStatus("Main_Upload_Images 11");
//                File file = new File(mobilepath);
//                functionsCall.logStatus("Main_Upload_Images 12");
//                String testName = file.getName();
//                functionsCall.logStatus("Main_Upload_Images 13");
//                fis = new FileInputStream(file);
//                functionsCall.logStatus("Main_Upload_Images 14");
//                result = client.storeFile(testName, fis);
//                functionsCall.logStatus("Main_Upload_Images 15");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            functionsCall.logStatus("Main_Upload_Images 16");
//            try {
//                functionsCall.logStatus("Main_Upload_Images 17");
//                client.logout();
//                functionsCall.logStatus("Main_Upload_Images 18");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            functionsCall.logStatus("Main_Upload_Images 19");
//            if (result)
//                handler.sendEmptyMessage(FILE_UPLOADED);
//            else handler.sendEmptyMessage(FILE_UPLOADED_ERROR);
//            functionsCall.logStatus("Main_Upload_Images 20");
//            return null;
//        }
//    }
//**********************************download file from FTP********************************************************************
    @SuppressLint("StaticFieldLeak")
    public class Download_file extends AsyncTask<String, String, String> {
        String downloadfile;
        String mobilepath = functionsCall.filepath("Documents") + File.separator;
        FileOutputStream fos = null;
        Handler handler;
        boolean file_downloaded = false, download_file = false;

        public Download_file(String downloadfile, Handler handler) {
            this.downloadfile = downloadfile;
            this.handler = handler;
        }

        @Override
        protected String doInBackground(String... params) {
            functionsCall.logStatus("TIC_tool_Download 1");
            FTPClient ftp_1 = new FTPClient();
            functionsCall.logStatus("TIC_tool_Download 2");
            try {
                functionsCall.logStatus("TIC_tool_Download 3");
                ftp_1.connect(FTP_HOST, FTP_PORT);
                functionsCall.logStatus("TIC_tool_Download 4");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                functionsCall.logStatus("TIC_tool_Download 5");
                ftp_1.login(FTP_USER, FTP_PASS);
                download_file = ftp_1.login(FTP_USER, FTP_PASS);
                functionsCall.logStatus("TIC_tool_Download 6");
            } catch (FTPConnectionClosedException e) {
                e.printStackTrace();
                try {
                    download_file = false;
                    ftp_1.disconnect();
                    handler.sendEmptyMessage(DOWNLOAD_FILE_ERROR);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (download_file) {
                functionsCall.logStatus("Ticketing Tool downloading file true...");
                try {
                    functionsCall.logStatus("TIC_tool_Download 7");
                    ftp_1.setFileType(FTP.BINARY_FILE_TYPE);
                    ftp_1.enterLocalPassiveMode();
                    functionsCall.logStatus("TIC_tool_Download 8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    functionsCall.logStatus("TIC_tool_Download 9");
                    ftp_1.changeWorkingDirectory("/Android/Ticketing/");
                    functionsCall.logStatus("TIC_tool_Download 10");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    functionsCall.logStatus("TIC_tool_Download 11");
                    FTPFile[] ftpFiles = ftp_1.listFiles("/Android/Ticketing/");  // storage path
                    functionsCall.logStatus("TIC_tool_Download 12");
                    int length = ftpFiles.length;
                    functionsCall.logStatus("TIC_tool_Download 13");
                    functionsCall.logStatus("TIC_tool_Download_length = " + length);
                    for (FTPFile ftpFile : ftpFiles) {
                        String namefile = ftpFile.getName();
                        functionsCall.logStatus("TIC_tool_Download_namefile : " + namefile);
                        boolean isFile = ftpFile.isFile();
                        if (isFile) {
                            if (namefile.equals(downloadfile)) {
                                functionsCall.logStatus("TIC_tool_Download File found to download");
                                try {
                                    fos = new FileOutputStream(mobilepath + downloadfile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    file_downloaded = ftp_1.retrieveFile("/Android/Ticketing/" + downloadfile, fos);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            download_file = false;
            try {
                ftp_1.logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (file_downloaded) {
                file_downloaded = false;
                handler.sendEmptyMessage(DOWNLOAD_FILE_SUCCESS);
            } else handler.sendEmptyMessage(DOWNLOAD_FILE_FAILURE);
        }
    }

    //******************************************* Download_apk ***************************************************************
    @SuppressLint("StaticFieldLeak")
    public class Download_apk extends AsyncTask<String, Integer, String> {
        boolean downloadapk = false, file_found = false;
        Handler handler;
        ProgressDialog progressDialog;
        String mobilepath = functionsCall.filepath("ApkFolder") + File.separator;
        String update_version;

        public Download_apk(Handler handler, ProgressDialog progressDialog, String update_version) {
            this.handler = handler;
            this.progressDialog = progressDialog;
            this.update_version = update_version;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functionsCall.showprogressdialog("Downloading...","Downloading apk file please wait...", progressDialog);
        }

        @Override
        protected String doInBackground(String... params) {
            int count;
            long read = 0;

            functionsCall.logStatus("Main_Apk 1");
            FTPClient ftp_1 = new FTPClient();
            functionsCall.logStatus("Main_Apk 2");
            try {
                functionsCall.logStatus("Main_Apk 3");
                ftp_1.connect(FTP_HOST, FTP_PORT);
                functionsCall.logStatus("Main_Apk 4");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                functionsCall.logStatus("Main_Apk 5");
                ftp_1.login(FTP_USER, FTP_PASS);
                downloadapk = ftp_1.login(FTP_USER, FTP_PASS);
                functionsCall.logStatus("Main_Apk 6");
            } catch (FTPConnectionClosedException e) {
                e.printStackTrace();
                try {
                    downloadapk = false;
                    ftp_1.disconnect();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (downloadapk) {
                functionsCall.logStatus("Apk download billing_file true");
                try {
                    functionsCall.logStatus("Main_Apk 7");
                    ftp_1.setFileType(FTP.BINARY_FILE_TYPE);
                    ftp_1.enterLocalPassiveMode();
                    functionsCall.logStatus("Main_Apk 8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    functionsCall.logStatus("Main_Apk 9");
                    ftp_1.changeWorkingDirectory("/Android/Ticketing/APK/");
                    functionsCall.logStatus("Main_Apk 10");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    functionsCall.logStatus("Main_Apk 11");
                    FTPFile[] ftpFiles = ftp_1.listFiles("/Android/Ticketing/APK/");
                    functionsCall.logStatus("Main_Apk 12");
                    int length = ftpFiles.length;
                    functionsCall.logStatus("Main_Apk 13");
                    functionsCall.logStatus("Main_Apk_length = " + length);
                    String namefile;
                    long filelength = 0;
                    for (FTPFile ftpFile : ftpFiles) {
                        namefile = ftpFile.getName();
                        functionsCall.logStatus("Main_Apk_namefile : " + namefile);
                        boolean isFile = ftpFile.isFile();
                        if (isFile) {
                            functionsCall.logStatus("Main_Apk_File: " + "Ticketing_app_" + update_version + ".apk");
                            if (namefile.equals("Ticketing_app_" + update_version + ".apk")) {
                                functionsCall.logStatus("Main_Apk File found to download");
                                filelength = ftpFile.getSize();
                                file_found = true;
                                break;
                            }
                        }
                    }
                    if (file_found) {
                        File file = new File(mobilepath + "Ticketing_app_" + update_version + ".apk");
                        functionsCall.logStatus("FTP File length: " + filelength);
                        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                        InputStream inputStream = ftp_1.retrieveFileStream("/Android/Ticketing/APK/" + "Ticketing_app_" + update_version + ".apk");
                        byte[] bytesIn = new byte[1024];
                        while ((count = inputStream.read(bytesIn)) != -1) {
                            read += count;
                            publishProgress((int) ((read * 100) / filelength));
                            outputStream.write(bytesIn, 0, count);
                        }
                        inputStream.close();
                        outputStream.close();

                        if (ftp_1.completePendingCommand()) {
                            functionsCall.logStatus("Apk file Download successfully.");
                            handler.sendEmptyMessage(APK_FILE_DOWNLOADED);
                        }
                    } else handler.sendEmptyMessage(APK_FILE_NOT_FOUND);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                ftp_1.logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }
    }
}
