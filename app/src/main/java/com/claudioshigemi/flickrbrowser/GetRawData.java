package com.claudioshigemi.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Claudio on 02/12/2017.
 */

/**
 * IDLE = Não está processando nada neste momento.
 * PROCESSING = Neste estágio está baixando os dados.
 * NOT_INITIALISED = Não conseguimos um URL válido.
 * FAILED_OR_EMPTY = O dado poder voltado vazio.
 * OK = Conseguiu dados válidos.
 */

enum DownloadStatus {
    IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK
}

class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";
    private final OnDownloadComplete mCallBack;
    //  m = de "m"ember variavel
    private DownloadStatus mdownloadStatus;

    public GetRawData(OnDownloadComplete callBack) {
        this.mdownloadStatus = DownloadStatus.IDLE;
        mCallBack = callBack;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute:  parametro = " + s);
        if (mCallBack != null) {
            mCallBack.onDownloadComplete(s, mdownloadStatus);
        }
        Log.d(TAG, "onPostExecute:  terminou.");
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            mdownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }
        try {
            mdownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: O código de resposta foi :" + response);
            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while (null != (line = reader.readLine())) {
                result.append(line).append("\n");
            }
            mdownloadStatus = DownloadStatus.OK;
            return result.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalido URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception reading data: " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground:  Excessão de Segurança. Precisa de Permissão? " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Erro ao fechar conexão : " + e.getMessage());
                }
            }
        }
        mdownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    void runInSameThread(String s) {
        Log.d(TAG, "runInSameThread:  Começou o método. ");
        onPostExecute(doInBackground(s));
        Log.d(TAG, "runInSameThread: Terminou o método. ");

    }

    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }
}







































