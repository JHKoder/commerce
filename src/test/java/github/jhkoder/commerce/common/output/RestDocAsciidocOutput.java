package github.jhkoder.commerce.common.output;

import github.jhkoder.commerce.common.error.ErrorDescriptor;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class RestDocAsciidocOutput {

    public void out(String domain, AdocRequest request) {
        out(domain,request.descriptorList(), request);

    }

    public void out(String domain, List<ErrorDescriptor> descriptorList,AdocRequest request) {
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

        String role ="";

        System.out.println(request.isRequestBody()+","+request.isRequestFields());
        String content = ":basedir: {docdir}/../../../\n" +
                ":snippets: {basedir}/build/generated-snippets\n" +
                ":icons: font\n" +
                ":source-highlighter: highlightjs\n" +
                ":toc: left\n" +
                ":toclevels: 3\n" +
                ":domain: " + domain + "\n\n" +

                ":isRequestBody: " + request.isRequestBody() + System.lineSeparator() +
                ":isRequestFields: " + request.isRequestFields() + System.lineSeparator() +
                ":isResponseBody: " + request.isResponseBody() + System.lineSeparator() +
                ":isResponseFields: " + request.isResponseFields() + System.lineSeparator() +

                "* 권한 : " + role +System.lineSeparator()+System.lineSeparator()+
                "---" + System.lineSeparator() +System.lineSeparator() +
                "===== Request\n" +
                "ifeval::[{isRequestBody} == true]\n" +
                "====== Body\n" +
                "include::{snippets}/{domain}/request-body.adoc[]\n" +
                "endif::[]\n" +
                "ifeval::[{isRequestFields} == true]\n" +
                "====== fields\n" +
                "include::{snippets}/{domain}/request-fields.adoc[]\n" +
                "endif::[]\n" +
                "===== Response\n" +
                "ifeval::[{isResponseBody} == true]\n" +
                "====== body\n" +
                "include::{snippets}/{domain}/response-body.adoc[]\n" +
                "endif::[]\n" +
                "ifeval::[{isResponseFields} == true]\n" +
                "====== fields\n" +
                "include::{snippets}/{domain}/response-fields.adoc[]\n" +
                "endif::[]\n\n";




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
                    """.formatted(descriptor.code(), descriptor.description());
        }
        return content + "|======";
    }
}
