package com.techassist.backend.config;

import com.techassist.backend.model.KnowledgeArticle;
import com.techassist.backend.repository.KnowledgeArticleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataSeeder — inserts default knowledge articles on first startup.
 * Only runs if the knowledge_articles table is empty.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final KnowledgeArticleRepository articleRepository;

    public DataSeeder(KnowledgeArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public void run(String... args) {
        if (articleRepository.count() > 0) {
            System.out.println("[DataSeeder] Articles already exist. Skipping seed.");
            return;
        }

        System.out.println("[DataSeeder] Seeding knowledge base articles...");

        articleRepository.save(KnowledgeArticle.builder()
                .title("VPN Troubleshooting Guide")
                .problem("User cannot connect to the company VPN.")
                .symptoms("Error 809, timeout, connection drops, no internet.")
                .steps("1. Verify general internet works.\n" +
                       "2. Restart the VPN client.\n" +
                       "3. Check VPN credentials.\n" +
                       "4. Disable and re-enable the VPN adapter.\n" +
                       "5. Check firewall settings.\n" +
                       "6. Reinstall the VPN client if needed.")
                .resolution("Usually resolved by restarting the client or updating credentials.")
                .category("Network")
                .tags("vpn, network, remote, error809")
                .build());

        articleRepository.save(KnowledgeArticle.builder()
                .title("DNS Troubleshooting Guide")
                .problem("User cannot resolve internal or external hostnames.")
                .symptoms("Websites fail to load, nslookup fails, ping by IP works but not by name.")
                .steps("1. Run ipconfig /flushdns.\n" +
                       "2. Check DNS servers with ipconfig /all.\n" +
                       "3. Test with nslookup google.com.\n" +
                       "4. Try nslookup against 8.8.8.8.\n" +
                       "5. Verify DNS server is reachable.\n" +
                       "6. Restart DNS Client service.")
                .resolution("Flushing DNS cache or restarting the DNS Client service usually fixes it.")
                .category("Network")
                .tags("dns, network, resolve, hostname")
                .build());

        articleRepository.save(KnowledgeArticle.builder()
                .title("Active Directory Account Lockout")
                .problem("User account keeps getting locked out repeatedly.")
                .symptoms("Cannot log in, account locked after several attempts, lockouts recurring.")
                .steps("1. Unlock account in ADUC.\n" +
                       "2. Check Event Viewer for lockout source (Event ID 4740).\n" +
                       "3. Identify the device causing lockouts.\n" +
                       "4. Check for cached credentials (Credential Manager).\n" +
                       "5. Check mobile devices with old passwords.\n" +
                       "6. Reset the password and update everywhere.")
                .resolution("Identify the lockout source and update stale credentials.")
                .category("Account & Access")
                .tags("ad, account, lockout, password, security")
                .build());

        articleRepository.save(KnowledgeArticle.builder()
                .title("Windows Network Troubleshooting")
                .problem("Network connectivity issues on Windows.")
                .symptoms("No internet, slow connection, unstable Wi-Fi, IP conflict.")
                .steps("1. Run ipconfig /all to check IP, gateway, DNS.\n" +
                       "2. Run ipconfig /release and ipconfig /renew.\n" +
                       "3. Ping the gateway and 8.8.8.8.\n" +
                       "4. Reset Winsock: netsh winsock reset.\n" +
                       "5. Reset TCP/IP: netsh int ip reset.\n" +
                       "6. Restart the network adapter.")
                .resolution("Winsock reset or IP release/renew typically resolves connectivity issues.")
                .category("Network")
                .tags("network, connectivity, ip, winsock")
                .build());

        articleRepository.save(KnowledgeArticle.builder()
                .title("Shared Folder Permissions")
                .problem("User cannot access a shared folder on the network.")
                .symptoms("Access denied, folder not found, credentials prompt.")
                .steps("1. Verify network connectivity.\n" +
                       "2. Check the UNC path.\n" +
                       "3. Check user's group membership.\n" +
                       "4. Verify Share permissions.\n" +
                       "5. Verify NTFS permissions.\n" +
                       "6. Clear cached credentials.\n" +
                       "7. Test access with another user.")
                .resolution("Check both Share and NTFS permissions — both must allow access.")
                .category("Account & Access")
                .tags("shared folder, permissions, ntfs, share")
                .build());

        articleRepository.save(KnowledgeArticle.builder()
                .title("Printer Troubleshooting")
                .problem("Printer is not printing or not found on the network.")
                .symptoms("Jobs stuck in queue, printer offline, driver errors.")
                .steps("1. Check printer power and network cable.\n" +
                       "2. Verify printer IP is reachable (ping).\n" +
                       "3. Clear the print queue.\n" +
                       "4. Restart the Print Spooler service.\n" +
                       "5. Remove and re-add the printer.\n" +
                       "6. Reinstall the latest driver.")
                .resolution("Restart Print Spooler or reinstall the printer/driver.")
                .category("Hardware")
                .tags("printer, hardware, spooler, driver")
                .build());

        System.out.println("[DataSeeder] ✅ Seeded 6 knowledge articles.");
    }
}