package com.notifly.backend.MailFormat;

import com.notifly.backend.JobPreferences.Entity.Job;
import org.springframework.stereotype.Component;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Getter
public class Mailers {

    // ── existing field — unchanged ────────────────────────────────────────
    private final String mailAfterUserRegistration =
        "<html>\n" +
        "  <body style='font-family: Arial, sans-serif; background-color: #f9fafb; padding: 20px;'>\n" +
        "    <table width='100%' cellpadding='0' cellspacing='0'>\n" +
        "      <tr>\n" +
        "        <td align='center'>\n" +
        "          <table width='600' style='background-color: #ffffff; padding: 25px; border-radius: 8px;'>\n" +
        "            <tr>\n" +
        "              <td>\n" +
        "                <h2 style='color:#2563eb;'>Welcome to Notifly {{userName}} 👋</h2>\n" +
        "                <p>Your registration was successful, and we're excited to have you with us!</p>\n" +
        "\n" +
        "                <p><strong>Notifly</strong> helps you stay ahead by delivering the latest job opportunities\n" +
        "                straight to you — smart alerts, timely updates, and zero duplicates.</p>\n" +
        "\n" +
        "                <h4>🔔 What you can expect:</h4>\n" +
        "                <ul>\n" +
        "                  <li>Daily job notifications at the right time</li>\n" +
        "                  <li>Alerts based on job titles you choose</li>\n" +
        "                  <li>Email, SMS, or WhatsApp delivery (as per your preference)</li>\n" +
        "                </ul>\n" +
        "\n" +
        "\n" +
        "                <p>If you need any help, feel free to reach out to us anytime.</p>\n" +
        "\n" +
        "                <p style='margin-top:25px;'>Warm regards,<br/>\n" +
        "                <strong>Team Notifly</strong><br/>\n" +
        "                <em>Your job alerts. Simplified.</em></p>\n" +
        "              </td>\n" +
        "            </tr>\n" +
        "          </table>\n" +
        "        </td>\n" +
        "      </tr>\n" +
        "    </table>\n" +
        "  </body>\n" +
        "</html>";


    // ── NEW: daily job alert email ────────────────────────────────────────
    public String jobAlertEmail(String userName, List<Job> jobs, String preferenceTitle) {

        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));

        String[][] avatarColors = {
            {"#dbeafe", "#1e40af"},
            {"#dcfce7", "#166534"},
            {"#fef3c7", "#92400e"},
            {"#ede9fe", "#6d28d9"},
            {"#fce7f3", "#9d174d"},
        };

        StringBuilder sb = new StringBuilder();

        // ── head + preheader
        sb.append("<!DOCTYPE html><html><head>")
          .append("<meta charset='UTF-8'/>")
          .append("<meta name='viewport' content='width=device-width,initial-scale=1.0'/>")
          .append("<title>Your Daily Job Alert</title>")
          .append("</head>")
          .append("<body style='margin:0;padding:0;background-color:#f1f5f9;font-family:-apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,sans-serif;'>")
          .append("<div style='display:none;font-size:1px;color:#f1f5f9;line-height:1px;max-height:0;overflow:hidden;'>")
          .append(jobs.size()).append(" new jobs matched your preferences today!</div>")

          // ── outer wrapper
          .append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0' style='background:#f1f5f9;padding:32px 16px;'>")
          .append("<tr><td align='center'>")
          .append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0' style='max-width:620px;'>")

          // ── header
          .append("<tr><td style='background:#0f172a;padding:24px 28px;border-radius:12px 12px 0 0;'>")
          .append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0'><tr>")
          .append("<td style='vertical-align:middle;'>")
          .append("<span style='color:#f8fafc;font-size:20px;font-weight:500;'>&#128276; Notifly</span>")
          .append("<div style='color:#64748b;font-size:12px;margin-top:4px;'>Your daily job digest</div>")
          .append("</td>")
          .append("<td style='text-align:right;vertical-align:middle;'>")
          .append("<span style='display:inline-block;background:rgba(37,99,235,0.25);color:#93c5fd;font-size:11px;font-weight:500;padding:4px 10px;border-radius:20px;border:1px solid rgba(147,197,253,0.25);'>")
          .append(jobs.size()).append(" new matches</span>")
          .append("</td></tr></table></td></tr>")

          // ── intro
          .append("<tr><td style='background:#ffffff;padding:22px 28px 16px;border-left:1px solid #e2e8f0;border-right:1px solid #e2e8f0;'>")
          .append("<h1 style='margin:0 0 6px;font-size:20px;font-weight:500;color:#0f172a;letter-spacing:-0.3px;'>")
          .append("Hi ").append(esc(userName)).append(", ").append(jobs.size()).append(" new jobs for you</h1>")
          .append("<p style='margin:0 0 3px;font-size:13px;color:#64748b;'>Preference: <strong style='color:#0f172a;font-weight:500;'>")
          .append(esc(preferenceTitle)).append("</strong></p>")
          .append("<p style='margin:0;font-size:12px;color:#94a3b8;'>").append(date).append("</p>")
          .append("</td></tr>")

          // thin divider
          .append("<tr><td style='background:#ffffff;padding:0 28px;border-left:1px solid #e2e8f0;border-right:1px solid #e2e8f0;'>")
          .append("<div style='height:1px;background:#f1f5f9;'></div></td></tr>");

        // ── job cards (N jobs, loops dynamically)
        for (int i = 0; i < jobs.size(); i++) {
            Job    job = jobs.get(i);
            String[] c = avatarColors[i % avatarColors.length];
            String  mt = (i == 0) ? "14px" : "10px";
            String  mb = (i == jobs.size() - 1) ? "14px" : "0";

            sb.append("<tr><td style='background:#ffffff;padding:0 28px;border-left:1px solid #e2e8f0;border-right:1px solid #e2e8f0;'>")
              .append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0' ")
              .append("style='border:1px solid #e2e8f0;border-radius:10px;margin:").append(mt).append(" 0 ").append(mb).append(";'>")
              .append("<tr><td style='padding:16px 18px;'>")
              .append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0'><tr>")

              // company avatar
              .append("<td style='width:48px;vertical-align:top;padding-right:14px;'>")
              .append("<div style='width:44px;height:44px;background:").append(c[0])
              .append(";border-radius:10px;text-align:center;line-height:44px;font-size:14px;font-weight:500;color:").append(c[1]).append(";'>")
              .append(initials(job.getCompany())).append("</div></td>")

              // job info
              .append("<td style='vertical-align:top;'>")
              .append("<div style='font-size:15px;font-weight:500;color:#0f172a;margin-bottom:3px;'>").append(esc(job.getTitle())).append("</div>")
              .append("<div style='font-size:13px;color:#64748b;margin-bottom:10px;'>").append(esc(job.getCompany())).append("</div>")
              .append("<table role='presentation' cellpadding='0' cellspacing='0'><tr>")

              // location chip
              .append("<td style='padding-right:6px;'>")
              .append("<span style='display:inline-block;font-size:11px;padding:3px 9px;border-radius:5px;background:#f8fafc;color:#64748b;border:1px solid #e2e8f0;'>")
              .append("&#128205; ").append(esc(job.getLocation())).append("</span></td>");

            // salary chip — only rendered when value is present
            if (job.getSalary() != null && !job.getSalary().isBlank()) {
                sb.append("<td style='padding-right:6px;'>")
                  .append("<span style='display:inline-block;font-size:11px;padding:3px 9px;border-radius:5px;background:#dcfce7;color:#166534;border:1px solid #bbf7d0;'>")
                  .append(esc(job.getSalary())).append("</span></td>");
            }

            // source chip + close meta row
            sb.append("<td>")
              .append("<span style='display:inline-block;font-size:11px;padding:3px 9px;border-radius:5px;background:#f8fafc;color:#94a3b8;border:1px solid #e2e8f0;'>")
              .append(esc(job.getSource())).append("</span></td>")
              .append("</tr></table></td>")

              // apply button
              .append("<td style='width:90px;vertical-align:middle;text-align:right;padding-left:14px;'>")
              .append("<a href='").append(esc(job.getJobUrl())).append("' ")
              .append("style='display:inline-block;padding:9px 16px;background:#2563eb;color:#ffffff;")
              .append("text-decoration:none;border-radius:8px;font-size:13px;font-weight:500;white-space:nowrap;'>")
              .append("Apply now</a></td>")

              .append("</tr></table>") // inner row
              .append("</td></tr>")    // card cell
              .append("</table>")      // card table
              .append("</td></tr>");   // outer cell
        }

        // ── CTA band
        sb.append("<tr><td style='background:#ffffff;padding:0 28px 20px;border-left:1px solid #e2e8f0;border-right:1px solid #e2e8f0;'>")
          .append("<div style='height:1px;background:#f1f5f9;margin-bottom:18px;'></div>")
          .append("<table role='presentation' width='100%' cellpadding='0' cellspacing='0'><tr>")
          .append("<td>")
          .append("<div style='font-size:13px;color:#64748b;'>Want more matches?</div>")
          .append("<div style='font-size:12px;color:#94a3b8;margin-top:2px;'>Update your preferences to broaden results.</div>")
          .append("</td>")
          .append("<td style='text-align:right;'>")
          .append("<a href='#' style='display:inline-block;padding:9px 16px;background:#0f172a;color:#ffffff;")
          .append("text-decoration:none;border-radius:8px;font-size:13px;font-weight:500;white-space:nowrap;'>")
          .append("Open dashboard</a>")
          .append("</td></tr></table></td></tr>")

          // ── footer
          .append("<tr><td style='background:#f8fafc;padding:18px 28px;border:1px solid #e2e8f0;border-top:none;border-radius:0 0 12px 12px;text-align:center;'>")
          .append("<p style='margin:0 0 10px;font-size:12px;color:#94a3b8;'>")
          .append("You're receiving this because you set up job alerts on Notifly.</p>")
          .append("<p style='margin:0;'>")
          .append("<a href='#' style='font-size:12px;color:#64748b;text-decoration:none;border-bottom:1px solid #cbd5e1;margin:0 8px;'>Manage preferences</a>")
          .append("<a href='#' style='font-size:12px;color:#64748b;text-decoration:none;border-bottom:1px solid #cbd5e1;margin:0 8px;'>Unsubscribe</a>")
          .append("</p>")
          .append("<p style='margin:12px 0 0;font-size:11px;color:#cbd5e1;'>")
          .append("Team Notifly &nbsp;&middot;&nbsp; Your job alerts. Simplified.</p>")
          .append("</td></tr>")

          .append("</table>")  // inner max-width table
          .append("</td></tr>")
          .append("</table>")  // outer wrapper table
          .append("</body></html>");

        return sb.toString();
    }
    
 public  String buildWhatsAppMessage(
        String userName,
        String preferenceTitle,
        String date,
        String dashboardUrl,
        List<Job> jobs) {

    StringBuilder sb = new StringBuilder();

    // Header
    sb.append("🔔 *Notifly — Daily Job Alert*\n\n");
    sb.append("Hi ").append(userName).append(", here are *")
      .append(jobs.size()).append(" new job").append(jobs.size() == 1 ? "" : "s")
      .append("* matched for you today!\n\n");

    sb.append("📌 Preference: *").append(preferenceTitle).append("*\n");
    sb.append("📅 ").append(date).append("\n");
    sb.append("\n━━━━━━━━━━━━━━━━━━━\n");

    // Job entries
    for (int i = 0; i < jobs.size(); i++) {
        Job job = jobs.get(i);

        sb.append("\n*").append(i + 1).append(". ").append(job.getTitle()).append("*\n");
        sb.append("🏢 ").append(job.getCompany()).append("\n");
        sb.append("📍 ").append(job.getLocation()).append("\n");

        // Salary — only if present (mirrors your email logic)
        if (job.getSalary() != null && !job.getSalary().isBlank()) {
            sb.append("💰 ").append(job.getSalary()).append(" | ");
            sb.append("🔗 ").append(job.getSource()).append("\n");
        }

        sb.append("👉 ").append(job.getJobUrl()).append("\n");

        // Divider between jobs, not after the last one
        if (i < jobs.size() - 1) {
            sb.append("\n─────────────────────\n");
        }
    }

    // Footer CTA
    sb.append("\n━━━━━━━━━━━━━━━━━━━\n\n");
    sb.append("🚀 Want more matches? Update your preferences:\n");
    sb.append("👉 ").append(dashboardUrl).append("\n\n");
    sb.append("_To stop alerts, reply *STOP*_");

    return sb.toString();
}

    // ── private helpers ───────────────────────────────────────────────────

    private String initials(String company) {
        if (company == null || company.isBlank()) return "??";
        String[] parts = company.trim().split("[\\s.]+");
        StringBuilder ini = new StringBuilder();
        for (int i = 0; i < Math.min(parts.length, 2); i++) {
            if (!parts[i].isEmpty()) ini.append(Character.toUpperCase(parts[i].charAt(0)));
        }
        return ini.length() > 0 ? ini.toString() : "??";
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}