package com.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Extrator {



    public static void main(String[] args) {
//        String q = String.format("%s %s %s",1,2,3);
        extrair();
    }

    public static void atualizar(){
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions o= new ChromeOptions();
        ChromeDriver driver = new ChromeDriver(o);
        driver.get("https://www.bible.com/pt-PT");
        Map<String, Integer> livros = obterLivros();
        String contentJs = lerScript();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[2]/div/a"))).click();

        for (String livro : livros.keySet()) {
            int totalCaps = livros.get(livro);
            for (int i = 1; i <= totalCaps; i++) {
                driver.get("https://www.bible.com/pt-PT/bible/1930/"+livro+"."+i+".NVT");
                List<Map<String,String>> resultJs = (List<Map<String,String>>)((JavascriptExecutor) driver).executeScript(contentJs);
                atualizarNoBanco(resultJs, "NVT");

                System.out.println();
            }
        }
    }

    public static void extrair(){
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions o= new ChromeOptions();
        ChromeDriver driver = new ChromeDriver(o);
        driver.get("https://www.bible.com/pt-PT");
        Map<String, Integer> livros = obterLivros();
        String contentJs = lerScript();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div[2]/div/a"))).click();

        for (String livro : livros.keySet()) {
            int totalCaps = livros.get(livro);
            for (int i = 1; i <= totalCaps; i++) {
                driver.get("https://www.bible.com/pt-PT/bible/1930/"+livro+"."+i+".NVT");
                List<Map<String,String>> resultJs = (List<Map<String,String>>)((JavascriptExecutor) driver).executeScript(contentJs);
                inserirNoBanco(resultJs, "NVT");

                System.out.println();
            }
        }
    }
    public static void atualizarNoBanco(List<Map<String,String>> dados, String versao){
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:src/main/resources/anotacoes_biblicas.db";
            conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();
            statement.setQueryTimeout(30);
            for (int i = 0; i < dados.size(); i++){
                String capitulo = dados.get(i).get("capitulo").replace("'","''");
                String livro = dados.get(i).get("livro").replace("'","''");
                String num_versiculo = dados.get(i).get("num_versiculo").replace("'","''");
                String snippet = dados.get(i).get("snippet").replace("'","''");
                String subtitulo = dados.get(i).get("subtitulo").replace("'","''");
                String versiculo = dados.get(i).get("versiculo").replace("'","''");
                String subtitulo_italico = dados.get(i).get("subtitulo_italico").replace("'","''");
                statement.executeUpdate(String.format("update biblia set subtitulo = '%s', subtitulo_italico = '%s' where versao = '%s' and livro = '%s' and capitulo = '%s' and num_versiculo = '%s'", subtitulo, subtitulo_italico, versao, livro, capitulo, num_versiculo));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void inserirNoBanco(List<Map<String,String>> dados, String versao){
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:src/main/resources/bancos/anotacoes_biblicas.db";
            conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();
            statement.setQueryTimeout(30);
            for (int i = 0; i < dados.size(); i++){
                String ordem = dados.get(i).get("ordem");
                String capitulo = dados.get(i).get("capitulo").replace("'","''");
                String livro = dados.get(i).get("livro").replace("'","''");
                String num_versiculo = dados.get(i).get("num_versiculo").replace("'","''");
                String snippet = dados.get(i).get("snippet").replace("'","''");
                String subtitulo = dados.get(i).get("subtitulo").replace("'","''");
                String versiculo = dados.get(i).get("versiculo").replace("'","''");
                String subtitulo_italico = dados.get(i).get("subtitulo_italico").replace("'","''");
                statement.executeUpdate(String.format("insert into biblia (versao,livro,capitulo,subtitulo,subtitulo_italico,versiculo,num_versiculo,snippet,paragrafo) values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %s)", versao, livro, capitulo, subtitulo, subtitulo_italico, versiculo,num_versiculo,snippet, ordem));
            }
        } catch (SQLException e) {
            String t = "";

            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static String lerScript(){
        try {
            return new String(Files.readAllBytes(Path.of("src/main/resources/extrator01.js")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Integer> obterLivros() {
        Map<String, Integer> livros = new LinkedHashMap<>();
        livros.put("GEN", 50);
        livros.put("EXO", 40);
        livros.put("LEV", 27);
        livros.put("NUM", 36);
        livros.put("DEU", 34);
        livros.put("JOS", 24);
        livros.put("JDG", 21);
        livros.put("RUT", 4);
        livros.put("1SA", 31);
        livros.put("2SA", 24);
        livros.put("1KI", 22);
        livros.put("2KI", 25);
        livros.put("1CH", 29);
        livros.put("2CH", 36);
        livros.put("EZR", 10);
        livros.put("NEH", 13);
        livros.put("EST", 10);
        livros.put("JOB", 42);
        livros.put("PSA", 150);
        livros.put("PRO", 31);
        livros.put("ECC", 12);
        livros.put("SNG", 8);
        livros.put("ISA", 66);
        livros.put("JER", 52);
        livros.put("LAM", 5);
        livros.put("EZK", 48);
        livros.put("DAN", 12);
        livros.put("HOS", 14);
        livros.put("JOL", 3);
        livros.put("AMO", 9);
        livros.put("OBA", 1);
        livros.put("JON", 4);
        livros.put("MIC", 7);
        livros.put("NAM", 3);
        livros.put("HAB", 3);
        livros.put("ZEP", 3);
        livros.put("HAG", 2);
        livros.put("ZEC", 14);
        livros.put("MAL", 4);
        livros.put("MAT", 28);
        livros.put("MRK", 16);
        livros.put("LUK", 24);
        livros.put("JHN", 21);
        livros.put("ACT", 28);
        livros.put("ROM", 16);
        livros.put("1CO", 16);
        livros.put("2CO", 13);
        livros.put("GAL", 6);
        livros.put("EPH", 6);
        livros.put("PHP", 4);
        livros.put("COL", 4);
        livros.put("1TH", 5);
        livros.put("2TH", 3);
        livros.put("1TI", 6);
        livros.put("2TI", 4);
        livros.put("TIT", 3);
        livros.put("PHM", 1);
        livros.put("HEB", 13);
        livros.put("JAS", 5);
        livros.put("1PE", 5);
        livros.put("2PE", 3);
        livros.put("1JN", 5);
        livros.put("2JN", 1);
        livros.put("3JN", 1);
        livros.put("JUD", 1);
        livros.put("REV", 22);
        return livros;
    }
}
