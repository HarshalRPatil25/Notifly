package com.notifly.backend.MailFormat;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class Mailers {

 private final   String  mailAfterUserRegistration =
        "<html>\n" +
        "  <body style='font-family: Arial, sans-serif; background-color: #f9fafb; padding: 20px;'>\n" +
        "    <table width='100%' cellpadding='0' cellspacing='0'>\n" +
        "      <tr>\n" +
        "        <td align='center'>\n" +
        "          <table width='600' style='background-color: #ffffff; padding: 25px; border-radius: 8px;'>\n" +
        "            <tr>\n" +
        "              <td>\n" +
        "                <h2 style='color:#2563eb;'>Welcome to Notifly {{userName}} 👋</h2>\n" +
        "                <p>Your registration was successful, and we’re excited to have you with us!</p>\n" +
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


}
