package com.ocr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.json.JSONObject;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public class Ocr {

	public static void main(String[] args) {
		try {
			diretorio("C:\\Users\\LVTLWF631\\Downloads\\test", "C:\\Users\\LVTLWF631\\Downloads\\convertidos1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String obterVariaveis() {
		Map<String, String> env = System.getenv();
		StringBuilder sb = new StringBuilder();
		for (String envName : env.keySet()) {
			sb.append(envName+"="+env.get(envName)+"\n");
		    System.out.format("%s=%s%n", envName, env.get(envName));
		}
		return sb.toString();
	}
	
	public static File[] getFiles(String directory) {
		File[] directories = new File(directory).listFiles(File::isDirectory);
		File[] fs = new File(directory).listFiles(File::isFile);
		List<File> files = new ArrayList<File>();

		for (int i = 0; i < directories.length; i++) {
			for (int j = 0; j < directories[i].listFiles().length; j++) {
				File file = directories[i].listFiles()[j];
				if (file.isFile()) {
					files.add(file);
				}
			}
		}
		for (int i = 0; i < fs.length; i++) {
			File file = fs[i];
			if (file.isFile()) {
				files.add(file);
			}
		}
		return files.toArray(new File[files.size()]);
	}
	
	public static String getExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p) {
			return fileName.substring(i + 1).toLowerCase();
		}
		return null;
	}
	
	public static void diretorio(String dir1, String dir2) throws IOException {
		File[] files = getFiles(dir1);

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			System.out.println("Lendo arquivo.. " + (i + 1) + " de " + files.length + " - " + file.getAbsolutePath());

			String extension = getExtension(file.getAbsolutePath());
			String r = execute(Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())), extension);

			FileWriter myWriter = new FileWriter(dir2 + "/" + UUID.randomUUID().toString()+ file.getName().replace(".pdf", "") + ".json");
			myWriter.write(r == null ? "" : r);
			myWriter.close();
			
			if (r != null) System.out.println("sucesso");
		
		}
		System.out.println("o processo foi concluido");
	}

	public static String execute(String base64, String extension) {
		JSONObject o = new JSONObject();
		Date inicio = new Date();
		Date fim = null;
		String text = null;
		StringBuilder sb = new StringBuilder();
		try {
			byte[] bytes = Base64.getDecoder().decode(base64);
			// CONFIGURAÇÃO DE VARIÁVEIS
			ExecutorService executorService = Executors.newFixedThreadPool(5);
			List<Future<String>> futures = new ArrayList<Future<String>>();			
			List<Result> results = new ArrayList<Result>();			
			Tesseract tesseract = new Tesseract();
			tesseract.setLanguage("por");
			tesseract.setTessVariable("user_defined_dpi", "500");
			tesseract.setTessVariable("debug_file", "/dev/null");
			
			if (!extension.toLowerCase().trim().contains("pdf")) {
				ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
				BufferedImage buff = ImageIO.read(bs);
				
				text = tesseract.doOCR(buff).toLowerCase();

				results.add(regexCnh(text));
				results.add(regexRg(text));

				Collections.sort(results, new Comparator<Result>() {
					public int compare(Result o1, Result o2) {
						return Integer.compare(o2.getQtdResult(), o1.getQtdResult());
					}
				});

				if (results.size() == 1) {
					fim = new Date();
					long sub = fim.getTime()-inicio.getTime();
					o.put("type", results.get(0).getDocumentType());
					o.put("draw", false);
					o.put("values", results.get(0).getJson());
					o.put("time", TimeUnit.MILLISECONDS.toMinutes(sub) +":"+ TimeUnit.MILLISECONDS.toSeconds(sub) + ":" +TimeUnit.MILLISECONDS.toSeconds(sub)+"."+TimeUnit.MILLISECONDS.toMillis(sub));
					return o.toString();
				} else if (results.size() > 1) {
					fim = new Date();
					long sub = fim.getTime()-inicio.getTime();
					if (results.get(0).getQtdResult() != results.get(1).getQtdResult()) {
						o.put("type", results.get(0).getDocumentType());
						o.put("draw", false);
						o.put("values", results.get(0).getJson());
						o.put("time", TimeUnit.MILLISECONDS.toMinutes(sub) +":"+ TimeUnit.MILLISECONDS.toSeconds(sub) + ":" +TimeUnit.MILLISECONDS.toSeconds(sub)+"."+TimeUnit.MILLISECONDS.toMillis(sub));
						return o.toString();
					}
				}
			} else {

				final PDDocument document = PDDocument.load(bytes);
				final PDFRenderer pdfRenderer = new PDFRenderer(document);

				for (int pg = 0; pg < document.getNumberOfPages(); pg++) {
					final int p = pg;
					Future<String> future = executorService.submit(new Callable<String>() {

						@Override
						public String call() {
							try {
								BufferedImage buff = pdfRenderer.renderImageWithDPI(p, 300, ImageType.RGB);
								if (null != buff) {
									return tesseract.doOCR(buff);
								}
							} catch (IOException | TesseractException e1) {
								
							}
							return "";
						}
					});
					futures.add(future);
				}

				for (int i = 0; i < document.getNumberOfPages(); i++) {
					Future<String> future = (Future<String>) futures.get(i);
					sb.append(future.get().toLowerCase() + "\n");
				}
				
				executorService.shutdown();
				document.close();

				results.add(regexCnh(sb.toString()));
				results.add(regexRg(sb.toString()));

				Collections.sort(results, new Comparator<Result>() {
					public int compare(Result o1, Result o2) {
						return Integer.compare(o2.getQtdResult(), o1.getQtdResult());
					}
				});
				
				if (results.size() == 1) {
					fim = new Date();
					long sub = fim.getTime()-inicio.getTime();
					o.put("type", results.get(0).getDocumentType());
					o.put("draw", false);
					o.put("values", results.get(0).getJson());
					o.put("time", TimeUnit.MILLISECONDS.toMinutes(sub) +":"+ TimeUnit.MILLISECONDS.toSeconds(sub) + ":" +TimeUnit.MILLISECONDS.toSeconds(sub)+"."+TimeUnit.MILLISECONDS.toMillis(sub));
					executorService.shutdown();
					return o.toString();
				} else if (results.size() > 1) {
					fim = new Date();
					long sub = fim.getTime()-inicio.getTime();
					if (results.get(0).getQtdResult() != results.get(1).getQtdResult()) {
						o.put("type", results.get(0).getDocumentType());
						o.put("draw", false);
						o.put("values", results.get(0).getJson());
						o.put("time", TimeUnit.MILLISECONDS.toMinutes(sub) +":"+ TimeUnit.MILLISECONDS.toSeconds(sub) + ":" +TimeUnit.MILLISECONDS.toSeconds(sub)+"."+TimeUnit.MILLISECONDS.toMillis(sub));
						executorService.shutdown();
						return o.toString();
					}
				}				
				executorService.shutdown();
			}
		} catch (IOException | TesseractException | InterruptedException | ExecutionException e) {
			fim = new Date();
			long sub = fim.getTime()-inicio.getTime();
			o.put("type", "Other");
			o.put("draw", false);
			o.put("values", new String[] {});
			o.put("error", e.getMessage());
			o.put("reason_for_error", e.getCause());
			o.put("time", TimeUnit.MILLISECONDS.toMinutes(sub) +":"+ TimeUnit.MILLISECONDS.toSeconds(sub) + ":" +TimeUnit.MILLISECONDS.toSeconds(sub)+"."+TimeUnit.MILLISECONDS.toMillis(sub));
			return o.toString();
		}		
		return text == null ? sb.toString() : text;
	}

	public static String base64(String base64) {
		return base64;
	}
	
	/**Carregar biblioteca externa**/
    public static String loadLib(String path) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream in = classloader.getResourceAsStream("libopencv_java320.so");
            new File(System.getProperty("user.dir")+"/"+path+ "/"+new Date().getTime()+"/" + "libopencv_java320.so").mkdirs();
            File fileOut = new File(System.getProperty("user.dir")+"/"+path+ "/"+new Date().getTime()+"/" + "libopencv_java320.so");

            OutputStream out = FileUtils.openOutputStream(fileOut);
            IOUtils.copy(in, out);
            in.close();
            out.close();
            System.load(fileOut.toString());
            return "sucesso";
        } catch (Exception e) {
            return e.getCause().toString();
        }
    }
	
	public static String image() {
		byte[] bytes = Base64.getDecoder().decode("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");
				
		Tesseract tesseract = new Tesseract();
		File tessDataFolder = LoadLibs.extractTessResources("tessdata");
		tesseract.setDatapath(tessDataFolder.getAbsolutePath());
		tesseract.setLanguage("por");
		tesseract.setTessVariable("user_defined_dpi", "500");
		tesseract.setTessVariable("debug_file", "/dev/null");
		ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
		BufferedImage buff;
		try {
			buff = ImageIO.read(bs);
			return tesseract.doOCR(buff);
		} catch (IOException | TesseractException e) {
			return e.getCause().toString();
		}
		
	}
	
	public static String image1(String base64) {
		byte[] bytes = Base64.getDecoder().decode(base64);
				
		Tesseract tesseract = new Tesseract();
		File tessDataFolder = LoadLibs.extractTessResources("tessdata");
		tesseract.setDatapath(tessDataFolder.getAbsolutePath());
		tesseract.setLanguage("por");
		tesseract.setTessVariable("user_defined_dpi", "500");
		tesseract.setTessVariable("debug_file", "/dev/null");
		ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
		BufferedImage buff;
		try {
			buff = ImageIO.read(bs);
			return tesseract.doOCR(buff);
		} catch (IOException | TesseractException e) {
			return e.getCause().toString();
		}
	}
	
	public static Result regexRg(String text) {
		if (text == null)
			return null;
		String nome = "";
		String dataNascimento = "";
		String dataExpedicao = "";
		String numeroRg = "";
		String cpf = "";
		String filiacaoMae = "";
		String filiacaoPai = "";
		int qtd = 0;

		String[] regexs = new String[] { "[a][s][s][i][n][a][t][u][r][a][ ]?[d][o][ ]?[d][i][r][e][t][o][r]",
				"[v][áa][l][i][d][a][ ]?[e][m][ ]?[t][o][d][o][o ]+[t][e][r][r][i][t][oó][r][i][o][ ]?[n][a][c][i][o][n][a][l]",
				"[p][o][l][e][g][a][r][ ]?[d][i][r][e][i][t][o]",
				"[c][a][r][t][e][i][r][a][ ]?[d][e][ ]?[i][d][e][n][t][i][d][a][d][e]" };

		String regex = "(?s)(?:[f][i][l][i][a][cç][ãa][o].*?)(\\w{4,}\\s+\\w{2,}\\s+\\w{2,}).*?(\\w{4,}\\s+\\w{2,}\\s+\\w{2,})";
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 2) {
				filiacaoMae = matcher.group(2);
				filiacaoPai = matcher.group(1);
				qtd++;
			} else if (matcher.groupCount() >= 1) {
				filiacaoPai = matcher.group(1);
				qtd++;
			}
		}

		regex = "(?s)(?:data de nascimento.*?)(\\d+[\\/]\\d+[\\/]\\d{4})";
		pattern = Pattern.compile(regex, Pattern.MULTILINE);
		matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 1) {
				dataNascimento = matcher.group(1);
				qtd++;
			}
		}

		regex = "\\d{8}\\-\\d{2}|\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}|\\d{3}\\.\\d{6}\\-\\d{2}|\\d{11}|\\d{6}\\.\\d{5}";
		pattern = Pattern.compile(regex, Pattern.MULTILINE);
		matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 1) {
				cpf = matcher.group(1);
				qtd++;
			}
		}

		regex = "(\\d{1,}[\\ ,\\-,\\.,]?\\d{3}[\\ ,\\-,\\.,]?\\d{3}[\\ ,\\-,\\.,]?\\d?).*?(?=[f][i][l][i][a][cç][aã][o])";
		pattern = Pattern.compile(regex, Pattern.DOTALL);
		matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 1) {
				numeroRg = matcher.group(1);
				qtd++;
			}
		}

		for (int i = 0; i < regexs.length; i++) {
			String r = regexs[i];
			pattern = Pattern.compile(r, Pattern.DOTALL);
			matcher = pattern.matcher(text);
			if (matcher.find()) {
				qtd++;
			}
		}

		JSONObject o = new JSONObject();
		o.put("nome", nome);
		o.put("data_expedicao", dataExpedicao);
		o.put("numero_rg", numeroRg);
		o.put("cpf", cpf);
		o.put("mae", filiacaoMae);
		o.put("pai", filiacaoPai);
		o.put("data_nascimento", dataNascimento);
		Result result = new Result();
		result.setJson(o);
		result.setQtdResult(qtd);
		result.setDocumentType("rg");
		return result;
	}

	public static Result regexCnh(String text) {
		if (text == null)
			return null;
		String nome = "";
		String dataNascimento = "";
		String dataEmissao = "";
		String dataPrimeiraHabilitacao = "";
		String numeroRg = "";
		String numeroRegistro = "";
		String cpf = "";
		String filiacao = "";
		int qtd = 0;
		String[] regexs = new String[] { "permissão", "departamento nacional de trânsito",
				"carteira nacional de habilitação", "carteira nacional", "ministério das cidades" };

		String regex = "(?s)(?:data emissão.*?)(\\d{2}[\\/]\\d{2}[\\/]\\d{4})";
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 1) {
				dataEmissao = matcher.group(1);
				qtd++;
			}
		}

		regex = "(?s)(?:data nascimento.*?)(\\d{2}[\\/]\\d{2}[\\/]\\d{4})";
		pattern = Pattern.compile(regex, Pattern.MULTILINE);
		matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 1) {
				dataEmissao = matcher.group(1);
				qtd++;
			}
		}

		regex = "(?s)(?:cpf.*?)([\\d]+\\.*[\\d]+.[\\d]+[-/][\\d]+)";
		pattern = Pattern.compile(regex, Pattern.MULTILINE);
		matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 1) {
				cpf = matcher.group(1);
				qtd++;
			}
		}

		regex = "(?s)(?:habilitação.*?\\d{2}[\\/]\\d{2}[\\/]\\d{4}.*?)(\\d{2}[\\/]\\d{2}[\\/]\\d{4})";
		pattern = Pattern.compile(regex, Pattern.MULTILINE);
		matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 1) {
				dataPrimeiraHabilitacao = matcher.group(1);
				qtd++;
			}
		}

		regex = "(?s)(?:[f][i][l][i][a][cç][ãa][o].*?)(\\w{4,}.*?\\w{4,}.*?)";
		pattern = Pattern.compile(regex, Pattern.MULTILINE);
		matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 1) {
				filiacao = matcher.group(1);
				qtd++;
			}
		}

		regex = "(?s)(?:[n][º] [r][e][g][i][s][t][r][o].*?)(\\d{10,})";
		pattern = Pattern.compile(regex, Pattern.MULTILINE);
		matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 1) {
				numeroRegistro = matcher.group(1);
				qtd++;
			}
		}

		regex = "(?s)(?:[d][o][c][.][i][d][e][n][t][i][d][a][d][e].*?)(\\d{7,})";
		pattern = Pattern.compile(regex, Pattern.MULTILINE);
		matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 1) {
				numeroRg = matcher.group(1);
				qtd++;
			}
		}

		regex = "(\\w{4,}\\s+(\\w{4,}|\\w{2,})\\s+(\\w{4,}|\\w{2,})).*\\n.*?(?=[d][o][c][.][i][d][e][n][t][i][d][a][d][e])";
		pattern = Pattern.compile(regex, Pattern.MULTILINE);
		matcher = pattern.matcher(text);

		if (matcher.find()) {
			if (matcher.groupCount() >= 1) {
				nome = matcher.group(1);
				qtd++;
			}
		}

		for (int i = 0; i < regexs.length; i++) {
			String r = regexs[i];
			pattern = Pattern.compile(r, Pattern.DOTALL);
			matcher = pattern.matcher(text);
			if (matcher.find()) {
				qtd++;
			}
		}

		JSONObject o = new JSONObject();
		o.put("nome", nome);
		o.put("data_emissao", dataEmissao);
		o.put("data_primeira_habilitacao", dataPrimeiraHabilitacao);
		o.put("numero_rg", numeroRg);
		o.put("numero_registro", numeroRegistro);
		o.put("cpf", cpf);
		o.put("filiacao", filiacao);
		o.put("data_nascimento", dataNascimento);
		Result result = new Result();
		result.setJson(o);
		result.setQtdResult(qtd);
		result.setDocumentType("cnh");
		return result;
	}

}
