package com.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.io.FileUtils;

public class HttpDownload {

	public static void main(String[] args) throws IOException {
		try {
			iniciar(new String[] {"https://www.uol.com.br/"}, 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void iniciar(String[] urls, Integer threads) throws IOException, InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(threads);
		List<Future<InputStream>> futures = new ArrayList<Future<InputStream>>();
		
		for (int i = 0; i < urls.length; i++) {			
			String url = urls[i];

			Future<InputStream> future = executorService.submit(new Callable<InputStream>() {
				@Override
				public InputStream call() throws Exception {
					return download(url, null);
				}
			});
			
			futures.add(future);
		}
		
		for (int i = 0; i < urls.length; i++) {
			File file = new File("file"+(i+1));
			Future<InputStream> future = futures.get(i);
			InputStream inputStream = future.get();
			FileUtils.copyInputStreamToFile(inputStream, file);
			System.out.println((i+1)+" arquivo baixado");
		}
	}

	public static InputStream download(String url, File file) throws IOException {
		URL u = new URL(url);
		URLConnection connection = u.openConnection();
		connection.setRequestProperty("User-Agent",
				"Mozilla 5.0 (Windows; U; " + "Windows NT 5.1; en-US; rv:1.8.0.11) ");
		return connection.getInputStream();
	}

}
