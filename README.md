# STEPS TO SETUP SPRING REST DOCS

- add dependency
- ```xml

          <dependency>
            <groupId>org.springframework.restdocs</groupId>
            <artifactId>spring-restdocs-mockmvc</artifactId>
            <scope>test</scope>
        </dependency>
   ```
- add plugin
- ```xml
   
    <plugin>
                <groupId>org.asciidoctor</groupId>
                <artifactId>asciidoctor-maven-plugin</artifactId>
                <version>1.5.3</version>
                <executions>
                    <execution>
                        <id>generate-docs</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>process-asciidoc</goal>
                        </goals>
                        <configuration>
                            <backend>html</backend>
                            <doctype>book</doctype>
                        </configuration>
                    </execution>
                </executions>
                <dependencies>
                    <dependency>
                        <groupId>org.springframework.restdocs</groupId>
                        <artifactId>spring-restdocs-asciidoctor</artifactId>
                        <version>${spring-restdocs.version}</version>
                    </dependency>
                </dependencies>
            </plugin>
  ```

- add ascii doc directory and index.adoc to main folder

## Spring MockMvc Configuration
- see documentation :-> https://docs.spring.io/spring-restdocs/docs/2.0.3.RELEASE/reference/html5/#getting-started-documentation-snippets-setup
- replace below
- ```text
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
  ```
- with
- ```text
    import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
  ```
  
## Path Parameters
- ```java
     mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("v1/beer", pathParameters(
                        parameterWithName("beerId").description("UUID of desired beer to get")
                )));
    notice {beerId} matches with parameter name
  ```

## Query Paramters
- pay attention to param **.param("iscold", "yes")** and 
- **requestParameters(parameterWithName("iscold").description("Is Beer Cold Query param"))**
- ```java

            mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID().toString())
                        .param("iscold", "yes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("v1/beer",
                        pathParameters(
                        parameterWithName("beerId").description("UUID of desired beer to get")
                ),
                        requestParameters(
                                parameterWithName("iscold").description("Is Beer Cold Query param")
                        )));
  ```