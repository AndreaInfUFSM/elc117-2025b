import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Versão que ilustra algumas más práticas:
 * - só uma grande classe (god-class) com métodos static
 * - main faz quase tudo
 * - mistura: nomes de métodos / variáveis em inglês e português
 * - mistura: leitura do CSV + formatação de template + lógica de geração dos cards + persistência
 * - seleção de template dentro do método de geração do SVG
 * - geração de SVG muito redundante e ilegível
 * - nenhuma validação robusta
 * - apesar de tudo, funciona :-)
 */
public class CardGenerator {

    public static void main(String[] args) {
        String inputCsv = "conquistas.csv";           // hardcoded
        String outputFolder = "cards";                // hardcoded
        String templateChoice = args.length > 0 ? args[0] : "default"; // "default" (verde) ou "azul"

        try {
            List<String[]> linhas = readCsv(inputCsv);
            // mistura "geração múltipla" com "geração individual"
            for (String[] dados : linhas) {
                if (dados.length < 3) continue; // sem validação real
                String nome   = dados[0].trim();
                String titulo = dados[1].trim();
                String motivo = dados[2].trim();

                // quebra o título em 2 linhas no primeiro espaço 
                String[] parts = titulo.split(" ", 2);
                String titulo1 = parts[0];
                String titulo2 = (parts.length > 1) ? parts[1] : "";

                // gera SVG com template escolhido
                String svg = gerarSvg(titulo1, titulo2, nome, motivo, templateChoice);

                // salva SVG
                salvarSvg(outputFolder, nome, svg);
            }

            System.out.println("Cartões gerados em: " + Paths.get(outputFolder).toAbsolutePath());
        } catch (Exception e) {
            // tratamento genérico (swallow-ish) de exceções 
            // todas exceções caem aqui, por exemplo: arquivo não encontrado, etc.
            e.printStackTrace();
        }
    }

    // leitura CSV básica
    private static List<String[]> readCsv(String caminho) throws IOException {
        List<String[]> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.startsWith("Nome")) continue;
                // split limitado a 3 colunas (nome,titulo,motivo)
                String[] partes = linha.split(",", 3);
                lista.add(partes);
            }
        }
        return lista;
    }


private static String gerarSvg(String titulo1, String titulo2, String nome, String motivo, String template) {
        // === BLOCO 1: templates "default/verde" e "azul" compartilham um SVG único parametrizado ===
        // Cores default (template "verde")
        String bg1 = "#0F172A";
        String bg2 = "#111827";
        String accent1 = "#22C55E";
        String accent2 = "#16A34A";
        String descColor = "#CBD5E1";
        String circleR = "280";
        String titleY1 = "260";
        String titleY2 = "360";
        String iconScale = "14";
        String iconStroke = "2";

        // Se template "azul", troca paleta e faz pequenos ajustes visuais
        if ("azul".equalsIgnoreCase(template)) {
            bg1 = "#001C55";
            bg2 = "#003B99";
            accent1 = "#F97316";
            accent2 = "#EA580C";
            descColor = "#E0E7FF";
            circleR = "260";
            titleY1 = "240";
            titleY2 = "330";
            iconScale = "13";
            iconStroke = "2.2";

            // Código condicional para fins didáticos (NÃO faça isso em código real)
            // Condicionais aninhados pioram a legibilidade do código e dificultam sua manutenção, teste e confiabilidade        
            // Aqui ainda por cima isso é usado para alterar detalhes com constantes hardcoded (valores "mágicos", sem explicação)            
            if (titulo1.length() > 12) {
                titleY1 = "230";
            } else if (titulo2.isEmpty()) {
                titleY2 = "315";
            } else {
                // valor “mágico” gratuito
                iconStroke = "2.15";
            }
        }

        // Se NÃO for "vermelho", retorna aqui mesmo com o primeiro SVG (default/verde ou azul)
        if (!"vermelho".equalsIgnoreCase(template)) {
            return """
            <svg xmlns="http://www.w3.org/2000/svg" width="1080" height="1350" viewBox="0 0 1080 1350">
              <defs>
                <!-- Fundo em degradê vertical -->
                <linearGradient id="bg" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%%" stop-color="%s"/>
                  <stop offset="100%%" stop-color="%s"/>
                </linearGradient>

                <!-- Gradiente de destaque -->
                <linearGradient id="accent" x1="0" y1="0" x2="1" y2="1">
                  <stop offset="0%%" stop-color="%s"/>
                  <stop offset="100%%" stop-color="%s"/>
                </linearGradient>

                <style>
                  .title { font: 800 96px system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; fill: #F8FAFC; letter-spacing: 0.5px; }
                  .name  { font: 800 56px system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; fill: #F8FAFC; }
                  .desc  { font: 600 44px system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; fill: %s; }
                  .icon  { fill: none; stroke: #FFFFFF; stroke-width: %s; stroke-linecap: round; stroke-linejoin: round; }
                </style>
              </defs>

              <!-- Fundo -->
              <rect width="1080" height="1350" fill="url(#bg)"/>

              <!-- Título (2 linhas) -->
              <text class="title" x="540" y="%s" text-anchor="middle">%s</text>
              <text class="title" x="540" y="%s" text-anchor="middle">%s</text>

              <!-- Círculo central -->
              <circle cx="540" cy="720" r="%s" fill="url(#accent)"/>

              <!-- Ícone (path 24x24 obtido de https://lucide.dev/icons/, reposicionado e com scaling) -->
              <g transform="translate(540,720) scale(%s) translate(-12 -12)">
                <path class="icon" d="M10 14.66v1.626a2 2 0 0 1-.976 1.696A5 5 0 0 0 7 21.978"/>
                <path class="icon" d="M14 14.66v1.626a2 2 0 0 0 .976 1.696A5 5 0 0 1 17 21.978"/>
                <path class="icon" d="M18 9h1.5a1 1 0 0 0 0-5H18"/>
                <path class="icon" d="M4 22h16"/>
                <path class="icon" d="M6 9a6 6 0 0 0 12 0V3a1 1 0 0 0-1-1H7a1 1 0 0 0-1 1z"/>
                <path class="icon" d="M6 9H4.5a1 1 0 0 1 0-5H6"/>
              </g>

              <!-- Nome -->
              <text class="name" x="540" y="1100" text-anchor="middle">%s</text>

              <!-- Motivo -->
              <text class="desc" x="540" y="1170" text-anchor="middle">%s</text>
            </svg>
            """.formatted(
                    bg1, bg2,
                    accent1, accent2,
                    descColor, iconStroke,
                    titleY1, titulo1,
                    titleY2, titulo2,
                    circleR,
                    iconScale,
                    nome,
                    motivo
            );
        }

        // === BLOCO 2: template "vermelho" ===
        // Em vez de parametrizar, repetimos tudo (pior prática possível)
        // Observação: cores e posicionamentos mudam um pouco, mas a estrutura do SVG ainda é semelhante
        return """
        <svg xmlns="http://www.w3.org/2000/svg" width="1080" height="1350" viewBox="0 0 1080 1350">
          <defs>
            <linearGradient id="bg2" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%%" stop-color="#3B0000"/>
              <stop offset="100%%" stop-color="#111111"/>
            </linearGradient>

            <linearGradient id="accent2" x1="0" y1="0" x2="1" y2="1">
              <stop offset="0%%" stop-color="#EF4444"/>
              <stop offset="100%%" stop-color="#DC2626"/>
            </linearGradient>

            <style>
              /* repetimos tudo de novo: classes, tamanhos, fontes... */
              .title2 { font: 800 98px system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; fill: #FFFFFF; letter-spacing: 0.6px; }
              .name2  { font: 800 58px system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; fill: #FFE4E6; }
              .desc2  { font: 700 46px system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; fill: #FECACA; }
              .icon2  { fill: none; stroke: #FFF5F5; stroke-width: 2.4; stroke-linecap: round; stroke-linejoin: round; }
            </style>
          </defs>

          <!-- Fundo -->
          <rect width="1080" height="1350" fill="url(#bg2)"/>

          <!-- Título (2 linhas) com offsets hardcoded diferentes -->
          <text class="title2" x="540" y="250" text-anchor="middle">%s</text>
          <text class="title2" x="540" y="350" text-anchor="middle">%s</text>

          <!-- Dois círculos  -->
          <circle cx="540" cy="720" r="285" fill="url(#accent2)"/>
          <circle cx="540" cy="720" r="265" fill="none" stroke="#7F1D1D" stroke-width="10"/>

          <!-- Ícone diferente (path 24x24 obtido de https://lucide.dev/icons/, reposicionado e com scaling)-->
          <g transform="translate(540,720) scale(24) translate(-12 -12)">
            <path class="icon2" d="M11.051 7.616a1 1 0 0 1 1.909.024l.737 1.452a1 1 0 0 0 .737.535l1.634.256a1 1 0 0 1 .588 1.806l-1.172 1.168a1 1 0 0 0-.282.866l.259 1.613a1 1 0 0 1-1.541 1.134l-1.465-.75a1 1 0 0 0-.912 0l-1.465.75a1 1 0 0 1-1.539-1.133l.258-1.613a1 1 0 0 0-.282-.867l-1.156-1.152a1 1 0 0 1 .572-1.822l1.633-.256a1 1 0 0 0 .737-.535z"/>            
          </g>

          <!-- Nome e motivo com novas classes de style (desnecessário) -->
          <text class="name2" x="540" y="1105" text-anchor="middle">%s</text>
          <text class="desc2" x="540" y="1178" text-anchor="middle">%s</text>

          <!-- Elementos "decorativos" hardcoded -->
          <rect x="120" y="1180" width="180" height="6" fill="#7F1D1D"/>
          <rect x="780" y="1180" width="180" height="6" fill="#7F1D1D"/>
          <text x="540" y="1240" font-family="monospace" font-size="20" fill="#FECACA" text-anchor="middle">
            template: vermelho // NÃO REUTILIZAR este código (didático)
          </text>
        </svg>
        """.formatted(
                // Título em duas linhas
                titulo1, titulo2,
                // Nome e motivo
                nome, motivo
        );
    }


    // gera o SVG com base no template (longo if/else)
    private static String gerarSvg1(String titulo1, String titulo2, String nome, String motivo, String template) {
        // Cores default (template "verde")
        String bg1 = "#0F172A";
        String bg2 = "#111827";
        String accent1 = "#22C55E";
        String accent2 = "#16A34A";
        String descColor = "#CBD5E1";
        String circleR = "280";
        String titleY1 = "260";
        String titleY2 = "360";
        String iconScale = "14";
        String iconStroke = "2";

        // Se azul, troca paleta e faz pequenos ajustes visuais
        if ("azul".equalsIgnoreCase(template)) {
            bg1 = "#001C55";
            bg2 = "#003B99";
            accent1 = "#F97316";    
            accent2 = "#EA580C";
            descColor = "#E0E7FF";
            circleR = "260";        
            titleY1 = "240";
            titleY2 = "330";
            iconScale = "13";       
            iconStroke = "2.2";
        }

        // SVG “único” com variações por cor/posicionamento (intencionalmente simples)
        return """
        <svg xmlns="http://www.w3.org/2000/svg" width="1080" height="1350" viewBox="0 0 1080 1350">
          <defs>
            <!-- Fundo em degradê vertical -->
            <linearGradient id="bg" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%%" stop-color="%s"/>
              <stop offset="100%%" stop-color="%s"/>
            </linearGradient>

            <!-- Gradiente de destaque -->
            <linearGradient id="accent" x1="0" y1="0" x2="1" y2="1">
              <stop offset="0%%" stop-color="%s"/>
              <stop offset="100%%" stop-color="%s"/>
            </linearGradient>

            <style>
              .title { font: 800 96px system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; fill: #F8FAFC; letter-spacing: 0.5px; }
              .name  { font: 800 56px system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; fill: #F8FAFC; }
              .desc  { font: 600 44px system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; fill: %s; }
              .icon  { fill: none; stroke: #FFFFFF; stroke-width: %s; stroke-linecap: round; stroke-linejoin: round; }
            </style>
          </defs>

          <!-- Fundo -->
          <rect width="1080" height="1350" fill="url(#bg)"/>

          <!-- Título (2 linhas) -->
          <text class="title" x="540" y="%s" text-anchor="middle">%s</text>
          <text class="title" x="540" y="%s" text-anchor="middle">%s</text>

          <!-- Círculo central -->
          <circle cx="540" cy="720" r="%s" fill="url(#accent)"/>

          <!-- Ícone (path 24x24 obtido de https://lucide.dev/icons/, reposicionado e com scaling) -->
          <g transform="translate(540,720) scale(%s) translate(-12 -12)">
            <path class="icon" d="M10 14.66v1.626a2 2 0 0 1-.976 1.696A5 5 0 0 0 7 21.978"/>
            <path class="icon" d="M14 14.66v1.626a2 2 0 0 0 .976 1.696A5 5 0 0 1 17 21.978"/>
            <path class="icon" d="M18 9h1.5a1 1 0 0 0 0-5H18"/>
            <path class="icon" d="M4 22h16"/>
            <path class="icon" d="M6 9a6 6 0 0 0 12 0V3a1 1 0 0 0-1-1H7a1 1 0 0 0-1 1z"/>
            <path class="icon" d="M6 9H4.5a1 1 0 0 1 0-5H6"/>
          </g>

          <!-- Nome -->
          <text class="name" x="540" y="1100" text-anchor="middle">%s</text>

          <!-- Motivo -->
          <text class="desc" x="540" y="1170" text-anchor="middle">%s</text>
        </svg>
        """.formatted(
                bg1, bg2,
                accent1, accent2,
                descColor, iconStroke,
                titleY1, titulo1,
                titleY2, titulo2,
                circleR,
                iconScale,
                nome,
                motivo
        );
    }

    private static void salvarSvg(String pasta, String nome, String conteudo) throws IOException {
        Files.createDirectories(Paths.get(pasta));
        String nomeArquivo = pasta + "/" + nome.replaceAll("\\s+", "_") + ".svg";
        Files.writeString(Paths.get(nomeArquivo), conteudo);
    }
}
