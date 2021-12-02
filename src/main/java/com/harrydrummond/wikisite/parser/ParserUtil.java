package com.harrydrummond.wikisite.parser;

import fr.brouillard.oss.commonmark.ext.notifications.Notification;
import fr.brouillard.oss.commonmark.ext.notifications.NotificationsExtension;
import org.aspectj.weaver.ast.ASTNode;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.parser.Parser;
import org.commonmark.parser.block.ParserState;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

public final class ParserUtil {

    // According to commonmark docs, these are thread safe
    public static final Parser PARSER;
    public static final HtmlRenderer RENDERER;



    static {
        List<Extension> extensions = List.of(
                AutolinkExtension.create(),
                StrikethroughExtension.create(),
                HeadingAnchorExtension.create(),
                NotificationsExtension.create().withClassMapper(n -> n == Notification.ERROR ? "alert center-block alert-danger" : "alert center-block alert-" + n.name().toLowerCase()),
                TaskListItemsExtension.create());
        PARSER = Parser.builder()
                .extensions(extensions)
                .build();

        RENDERER = HtmlRenderer.builder()
                .escapeHtml(true)
                .sanitizeUrls(true)
                .extensions(extensions)
                .build();
    }

    public static String parse(String str) {
        return RENDERER.render(PARSER.parse(str));
    }
}