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

## Query Parameters
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
  
## Documenting Responses
- when documenting responses,
- if you miss on property, the test will complain
- pay attention to  **getBeerById()**
- ```java 
     responseFields(
                                fieldWithPath("id").description("Id of Beer"),
                                fieldWithPath("version").description("Version Number"),
                                fieldWithPath("createdDate").description("Date Created"),
                                fieldWithPath("lastModifiedDate").description("Date Updated"),
                                fieldWithPath("beerName").description("Beer Name"),
                                fieldWithPath("beerStyle").description("Beer Style"),
                                fieldWithPath("upc").description("UPC of Beer"),
                                fieldWithPath("price").description("Price"),
                                fieldWithPath("quantityOnHand").description("Quantity On hand")
                        )));
  ```

## Documenting Responses
- here we use **requestFields** instead of responseFields used for response
- for properties we don't pass, use **.ignored()**

## Documenting Validation Constraints
- create test resources folder (re-import maven)
- create directory one by one org > springframework > restdocs > templates
- add file request-fields.snippet
- input the below
- ```text
        |===
        |Path|Type|Description|Constraints

    {{#fields}}
        |{{path}}
        |{{type}}
        |{{description}}
        |{{constraints}}

    {{/fields}}
        |===
  ```
  
- running tests now we will get the below error
- org.springframework.restdocs.mustache.MustacheException$Context: No method or field with name 'constraints' on line X
- add code snippet to test class
- ```java
     private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input){
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path){
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
  ```
- FINALLY, declare snippet in class method ConstrainedFields fields = new ConstrainedFields(BeerDto.class);
- and  replace default **fieldWithPath** with the above snippets  **fields.withPath**
- run **mvn clean package**  and check target > generated-snippets > path defined in andDo(document(**))
- e.g  v1/beer in  .andDo(document("v1/beer" ...)