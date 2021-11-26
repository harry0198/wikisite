package com.harrydrummond.wikisite.parser;

import org.aspectj.weaver.ast.ASTNode;
import org.commonmark.Extension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

public final class ParserUtil {

    // According to commonmark docs, these are thread safe
    public static final Parser PARSER;
    public static final HtmlRenderer RENDERER;



    static {
        //List<Extension> notificationExtension = List.of(NotificationsExtension.create());
        PARSER = Parser.builder()
                .build();

        RENDERER = HtmlRenderer.builder()
                .escapeHtml(true)
                .sanitizeUrls(true)
                .build();
    }

    public static String parse(String str) {
        return RENDERER.render(PARSER.parse(str));
    }
}