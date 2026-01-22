CREATE TABLE short_urls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    short_key VARCHAR(10) NOT NULL UNIQUE,
    original_url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL
);

INSERT INTO short_urls (short_key, original_url, created_at, expires_at)
VALUES
    ('rs1Aed', 'https://www.sivalabs.in/code-offline-with-local-ai-ollama',
     '2024-07-15 00:00:00', '2024-07-15 00:00:00' + INTERVAL 7 DAY),

    ('hujfDf', 'https://www.sivalabs.in/run-spring-boot-testcontainers-tests-at-jet-speed',
     '2024-07-16 00:00:00', '2024-07-16 00:00:00' + INTERVAL 7 DAY),

    ('ertcbn', 'https://www.sivalabs.in/running-custom-spring-initializr',
     '2024-07-17 00:00:00', '2024-07-17 00:00:00' + INTERVAL 7 DAY),

    ('edfrtg', 'https://www.sivalabs.in/an-email-template-of-asking-for-technical-help',
     '2024-07-18 00:00:00', '2024-07-18 00:00:00' + INTERVAL 7 DAY),

    ('vbgtyh', 'https://www.sivalabs.in/mastering-spring-boot-in-5-stages',
     '2024-07-19 00:00:00', '2024-07-19 00:00:00' + INTERVAL 7 DAY),

    ('rtyfgb', 'https://www.sivalabs.in/thymeleaf-layouts-using-fragment-expressions',
     '2024-07-25 00:00:00', '2024-07-25 00:00:00' + INTERVAL 7 DAY),

    ('rtvbop', 'https://www.sivalabs.in/spring-ai-rag-using-embedding-models-vector-databases',
     '2024-07-26 00:00:00', '2024-07-26 00:00:00' + INTERVAL 7 DAY),

    ('2wedfg', 'https://www.sivalabs.in/getting-started-with-spring-ai-and-open-ai',
     '2024-07-27 00:00:00', '2024-07-27 00:00:00' + INTERVAL 7 DAY),

    ('6yfrd4', 'https://www.sivalabs.in/langchain4j-retrieval-augmented-generation-tutorial',
     '2024-07-28 00:00:00', '2024-07-28 00:00:00' + INTERVAL 7 DAY),

    ('r5t4tt', 'https://www.sivalabs.in/langchain4j-ai-services-tutorial',
     '2024-07-29 00:00:00', '2024-07-29 00:00:00' + INTERVAL 7 DAY),

    ('ffr4rt', 'https://www.sivalabs.in/generative-ai-conversations-using-langchain4j-chat-memory',
     '2024-08-10 00:00:00', '2024-08-10 00:00:00' + INTERVAL 7 DAY),

    ('9oui7u', 'https://www.sivalabs.in/getting-started-with-generative-ai-using-java-langchain4j-openai-ollama',
     '2024-08-11 00:00:00', '2024-08-11 00:00:00' + INTERVAL 7 DAY),

    ('cvbg5t', 'https://www.sivalabs.in/go-for-java-springboot-developers',
     '2024-08-12 00:00:00', '2024-08-12 00:00:00' + INTERVAL 7 DAY),

    ('nm6ytf', 'https://www.sivalabs.in/spring-boot-jooq-tutorial-fetching-many-to-many-associations',
     '2024-08-13 00:00:00', '2024-08-13 00:00:00' + INTERVAL 7 DAY),

    ('tt5y6r', 'https://www.sivalabs.in/spring-boot-jooq-tutorial-fetching-one-to-many-associations',
     '2024-08-14 00:00:00', '2024-08-14 00:00:00' + INTERVAL 7 DAY),

    ('fgghty', 'https://www.sivalabs.in/spring-boot-jooq-tutorial-fetching-one-to-one-associations',
     '2024-08-15 00:00:00', '2024-08-15 00:00:00' + INTERVAL 7 DAY),

    ('f45tre', 'https://www.sivalabs.in/spring-boot-jooq-tutorial-crud-operations',
     '2024-08-16 00:00:00', '2024-08-16 00:00:00' + INTERVAL 7 DAY),

    ('54rt54', 'https://www.sivalabs.in/spring-boot-jooq-tutorial-getting-started',
     '2024-08-17 00:00:00', '2024-08-17 00:00:00' + INTERVAL 7 DAY);

