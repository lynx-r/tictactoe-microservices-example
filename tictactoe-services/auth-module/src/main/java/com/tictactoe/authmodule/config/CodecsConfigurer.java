/*
 * © Copyright 2018 Aleksey Popryadukhin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tictactoe.authmodule.config;//
//package com.workingbit.article.config;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.TreeNode;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
//import org.bson.types.ObjectId;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.codec.ServerCodecConfigurer;
//import org.springframework.http.codec.json.Jackson2JsonDecoder;
//import org.springframework.http.codec.json.Jackson2JsonEncoder;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//import org.springframework.web.reactive.config.WebFluxConfigurer;
//
//import java.io.IOException;
//
//@Configuration
//public class CodecsConfigurer {
//
////  @Bean
////  Jackson2JsonEncoder jackson2JsonEncoder() {
////    return new Jackson2JsonEncoder(objectMapper());
////  }
////
////  @Bean
////  Jackson2JsonDecoder jackson2JsonDecoder() {
////    return new Jackson2JsonDecoder(objectMapper());
////  }
////
////  @Bean
////  WebFluxConfigurer webFluxConfigurer(Jackson2JsonEncoder encoder, Jackson2JsonDecoder decoder) {
////    return new WebFluxConfigurer() {
////      @Override
////      public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
////        configurer.defaultCodecs().jackson2JsonEncoder(encoder);
////        configurer.defaultCodecs().jackson2JsonDecoder(decoder);
////      }
////    };
////  }
//
//  @Bean
//  ObjectMapper objectMapper() {
//    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//    builder.deserializerByType(ObjectId.class, new JsonDeserializer<ObjectId>() {
//          @Override
//          public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//            TreeNode oid = p.readValueAsTree().get("$oid");
//            String string = oid.toString().replaceAll("\"", "");
//            return new ObjectId(string);
//          }
//        }
//    );
//    builder.serializerByType(ObjectId.class, new ToStringSerializer());
//    return builder.build();
//  }
//
//}
