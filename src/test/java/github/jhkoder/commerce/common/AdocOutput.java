package github.jhkoder.commerce.common;

import github.jhkoder.commerce.common.error.ErrorDescriptor;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class AdocOutput {
    protected void out(String domain, List<ErrorDescriptor> descriptorList) {
        String[] paths = domain.split("/");
        int lastNumber = paths.length - 1;
        String path;
        if(!domain.contains("/")){
            path = domain;
        }else{
            path = String.join("/", Arrays.copyOfRange(paths, 0, lastNumber));

        }
        String name = paths[lastNumber];
        String outputDir = "src/docs/asciidoc/" + path;

        String content = ":basedir: {docdir}/../../../\n" +
                ":snippets: {basedir}/build/generated-snippets\n" +
                ":icons: font\n" +
                ":source-highlighter: highlightjs\n" +
                ":toc: left\n" +
                ":toclevels: 3\n" +
                ":domain: " + domain + "\n\n" +
                "===== Request \n\n" +
                "====== parameter Json\n" +
                "include::{snippets}/{domain}/request-body.adoc[]\n\n" +
                "====== Request fields\n" +
                "include::{snippets}/{domain}/request-fields.adoc[]\n\n" +
                "====== curl\n" +
                "include::{snippets}/{domain}/curl-request.adoc[]\n\n" +
                "===== response\n" +
                "include::{snippets}/{domain}/http-response.adoc[]\n";

        content = errorTable(content, descriptorList);

        Path directoryPath = Paths.get(outputDir);
        Path filePath = Paths.get(outputDir, name + ".adoc");

        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            FileWriter writer = new FileWriter(filePath.toFile());
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while generating the file: " + e.getMessage());

        }
    }

    private String errorTable(String content, List<ErrorDescriptor> list) {
        if (list == null || list.isEmpty()) {
            return content;
        }
        content += "===== Error Code\n\n" +
                "[frame=none,width=70%,float=center]\n" +
                "|======\n" +
                "|Error Code 3+| 설명 \n\n";

        for (ErrorDescriptor descriptor : list) {
            content += """
                    |%d
                    3+|%s
                    """.formatted(descriptor.getCode(), descriptor.getAbout());
        }
        return content + "|======";
    }
}
