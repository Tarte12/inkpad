package org.example.demo3.domain.chatbot.prompt;

public enum ChatBotPromptTemplate {

    BLOG_DRAFT("""
        너는 기술 블로그 작성을 도와주는 AI야.
        주제: %s
        목적: %s
        대상 독자: %s
        조건: %s

        위 정보를 참고해서 블로그 글 초안을 작성해줘.
    """),

    RECOMMEND_TITLE("""
        너는 기술 블로그의 제목을 지어주는 AI야.
        다음 글을 읽고 독자의 흥미를 끌 수 있는 블로그 제목을 3~5개 추천해줘.
        각 제목은 30자 이내로 작성해줘.

        글 내용:
        %s
    """),

    CLEAN_SENTENCE("""
        너는 기술 블로그를 도와주는 AI야.
        다음 문장을 자연스럽고 깔끔하게 고쳐줘. 맞춤법, 띄어쓰기, 문장 흐름까지 고려해서 바꿔줘.

        문장:
        %s
    """);

    private final String template;

    ChatBotPromptTemplate(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}

