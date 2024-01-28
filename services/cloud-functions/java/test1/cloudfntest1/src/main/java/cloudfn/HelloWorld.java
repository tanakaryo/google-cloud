package cloudfn;


import java.io.BufferedWriter;
import java.io.IOException;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

public class HelloWorld implements HttpFunction {
    
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        BufferedWriter writer = httpResponse.getWriter();
        writer.write("Hello World!");
    }
}
