package com.josegd.contacts;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class WSCallDialogFragment extends DialogFragment {
	
	public static final String ARG_PHONE_NUMBER = "arg_phone_number";
	
	private static final String SERVICE_URL = "http://libphonenumber.appspot.com/phonenumberparser";
	private static final String ENCODING = "UTF-8";
	private static final String DELIMITER = "--";
	private static final String CR = "\n";
	private static final int BUFFER_SIZE = 4 * 1024;
	
	private View mResultView;
	
	@Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
		String phoneNumber = getArguments().getString(ARG_PHONE_NUMBER);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mResultView = inflater.inflate(R.layout.wscall_result, null);
      builder.setTitle(R.string.wscall)
      		 .setView(mResultView)
             .setPositiveButton(android.R.string.ok, null);
      TextView tvConnecting = (TextView) mResultView.findViewById(R.id.connecting);
		tvConnecting.setText(getActivity().getString(R.string.connecting) + phoneNumber + "...");
      new WebServiceCaller().execute(phoneNumber);
      return builder.create();
   }
	
	private class WebServiceCaller extends AsyncTask<String, Void, String> {

		private String boundary = DELIMITER + "AndroidClient" + DELIMITER + Long.toHexString(System.currentTimeMillis()); 
		
		@Override
		protected String doInBackground(String... params) {
			URL url = null;
			HttpURLConnection conn = null;
			try {
				try {
					url = new URL(SERVICE_URL);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Connection", "keep-alive");
					conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
					conn.setUseCaches(false);
					conn.setDoInput(true);
					conn.setDoOutput(true);
					byte[] outputInBytes = getRequestParamsString(params[0]).getBytes(ENCODING);
					conn.setRequestProperty("Content-Length", "" + outputInBytes.length);
					OutputStream os = conn.getOutputStream();
					os.write(outputInBytes);
					os.close();
					conn.connect();
					int respCode = conn.getResponseCode();
					String s = respCode + " - " + conn.getResponseMessage();
					InputStream stream = respCode < 400 ? conn.getInputStream() : conn.getErrorStream();  
					return s + "\n" + inputStreamToString(stream, ENCODING);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} finally {
				conn.disconnect();
			}
			return null;
		}
		
		protected void onPostExecute(String result) {
			TextView tvResult = (TextView) mResultView.findViewById(R.id.result);
			tvResult.setText(result);
		}
		
		// http://www.javacodegeeks.com/2013/06/android-http-client-get-post-download-upload-multipart-request.html
		
		private String getRequestParamsString(String phoneNumber) {
			String separator = boundary + CR;
			String s = separator + "Content-Disposition: form-data; name=\"phoneNumber\"" + CR + phoneNumber + CR;
			s += separator + "Content-Disposition: form-data; name=\"defaultCountry\"" + CR;
			s += separator + "Content-Disposition: form-data; name=\"languageCode\"" + CR;
			s += separator + "Content-Disposition: form-data; name=\"regionCode\"" + CR;
			s += separator + "Content-Disposition: form-data; name=\"numberFile\"; filename=\"\"" + CR +
				              "Content-Type: application/octet-stream" + CR + separator;
			return s;
		}
		
		// Taken from http://stackoverflow.com/a/25103484/858626
		
		private String inputStreamToString(InputStream inputStream, String charsetName) throws IOException {
			StringBuilder builder = new StringBuilder();
		   InputStreamReader reader = new InputStreamReader(inputStream, charsetName);
		   char[] buffer = new char[BUFFER_SIZE];
		   int length;
		   while ((length = reader.read(buffer)) != -1) {
		   	builder.append(buffer, 0, length);
		   }
		   return builder.toString();
		}
		
	}
	
}
