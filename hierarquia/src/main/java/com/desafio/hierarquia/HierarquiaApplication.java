package com.desafio.hierarquia;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class HierarquiaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HierarquiaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//      Verificação do comando
        if (args.length == 0) {
            System.out.println("Uso: java -jar target/cli.jar analyze -depth <n> -verbose (opcional) \"<phrase>\"");
            return;
        } else if ("analyze".equalsIgnoreCase(args[0])) {
            processAnalyze(args);
        } else {
            System.out.println("Use o comando 'analyze'!");
        }
    }

    private void processAnalyze(String[] args) {
        int depth = 0;
        boolean verbose = false;
        String phrase = "";

        for(int i = 1; i < args.length; i++) {
//          Switch para analisar cada argumento
            switch (args[i]) {
                case "-depth":
                    depth = Integer.parseInt(args[i + 1]);
                    break;
                case "-verbose":
                    verbose = true;
                    break;
                default:
                    phrase += args[i] + " ";
                    break;
            }
        }

        phrase = phrase.trim();

        Map<String, Object> hierarchy = loadHierarchy();

        if (hierarchy == null || hierarchy.isEmpty()) {
            System.out.println("Erro ao carregar Hierarquia");
        }

        if (depth < 1 || phrase.isEmpty()) {
            System.out.println("Uso: java -jar target/cli.jar analyze -depth <n> -verbose (opcional) \"<phrase>\"");
        } else {
            analyzePhrase(depth, verbose, phrase, hierarchy);
        }
    }

    private Map<String, Object> loadHierarchy() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("src/main/java/com/desafio/hierarquia/dicts/hierarquia.json");
            return mapper.readValue(file, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void analyzePhrase(int depth, boolean verbose, String phrase, Map<String, Object> hierarchy) {
        String[] words = phrase.toLowerCase().split("\\s+");
        Map<String, Integer> categoryCount = new HashMap<>();

        for (String word : words) {
            searchInHierarchy(word, hierarchy, depth, 1, categoryCount);
        }

//      Exibindo as categorias encontradas e suas contagens
        if (categoryCount.isEmpty()) {
            System.out.println("Nenhuma categoria encontrada na profundidade especificada.");
        } else {
            categoryCount.forEach((key, value) -> System.out.println(value + " " + key + " foram mencionados"));
        }
    }

    private void searchInHierarchy(String word, Map<String, Object> hierarchy, int depth, int currentDepth, Map<String, Integer> categoryCount) {
        if (currentDepth > depth) {
            return;
        }

        for (Map.Entry<String, Object> entry : hierarchy.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

//          Verifica se está no nível desejado e procura a palavra em suas subcategorias
            if (currentDepth == depth) {
                if (containsWord(word, value)) {
                    categoryCount.put(key, categoryCount.getOrDefault(key, 0) + 1);
                }
            } else if (value instanceof Map) {
//              Se o valor associado for um mapa continua a busca recursivamente
                searchInHierarchy(word, (Map<String, Object>) value, depth, currentDepth + 1, categoryCount);
            }
        }
    }

    private boolean containsWord(String word, Object value) {
        if (value instanceof Map) {
            Map<String, Object> subMap = (Map<String, Object>) value;
//          Verifica se a palavra existe em algum nível abaixo
            if (subMap.containsKey(word)) {
                return true;
            }
//          Busca recursiva dentro do mapa
            for (Object subValue : subMap.values()) {
                if (subValue instanceof Map && containsWord(word, subValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void verboseMetrics() {

    }
}


