package template;

public class SvgTemplate {

    private final String bgColor1;
    private final String bgColor2;
    private final String accent1;
    private final String accent2;

    private SvgTemplate(String bg1, String bg2, String accent1, String accent2) {
        this.bgColor1 = bg1;
        this.bgColor2 = bg2;
        this.accent1 = accent1;
        this.accent2 = accent2;
    }

    public static SvgTemplate fromChoice(String choice) {
        return switch (choice) {
            case "azul" -> new SvgTemplate("#001C55", "#003B99", "#2563EB", "#1E3A8A");
            default -> new SvgTemplate("#0F172A", "#111827", "#22C55E", "#16A34A");
        };
    }

    public String render(String title1, String title2, String name, String reason) {
        return String.format("""
        <svg xmlns='http://www.w3.org/2000/svg' width='1080' height='1350' viewBox='0 0 1080 1350'>
          <defs>
            <linearGradient id='bg' x1='0' y1='0' x2='0' y2='1'>
              <stop offset='0%%' stop-color='%s'/>
              <stop offset='100%%' stop-color='%s'/>
            </linearGradient>
            <linearGradient id='accent' x1='0' y1='0' x2='1' y2='1'>
              <stop offset='0%%' stop-color='%s'/>
              <stop offset='100%%' stop-color='%s'/>
            </linearGradient>
            <style>
              .title { font: 800 96px system-ui, sans-serif; fill: #F8FAFC; }
              .name  { font: 800 56px system-ui, sans-serif; fill: #F8FAFC; }
              .desc  { font: 600 44px system-ui, sans-serif; fill: #CBD5E1; }
            </style>
          </defs>

          <rect width='1080' height='1350' fill='url(#bg)'/>
          <text class='title' x='540' y='260' text-anchor='middle'>%s</text>
          <text class='title' x='540' y='360' text-anchor='middle'>%s</text>
          <circle cx='540' cy='720' r='280' fill='url(#accent)'/>
          <text class='name' x='540' y='1100' text-anchor='middle'>%s</text>
          <text class='desc' x='540' y='1170' text-anchor='middle'>%s</text>
        </svg>
        """, bgColor1, bgColor2, accent1, accent2, title1, title2, name, reason);
    }
}
