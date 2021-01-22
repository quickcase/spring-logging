package app.quickcase.logging.httpcomponents;

import app.quickcase.logging.HttpHeaders;
import app.quickcase.logging.MdcFields;
import app.quickcase.logging.tracing.RequestIdGenerator;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import java.util.function.Supplier;

public class OutboundRequestIdSettingInterceptor implements HttpRequestInterceptor {

    private final Supplier<String> nextRequestId;

    public OutboundRequestIdSettingInterceptor() {
        this(RequestIdGenerator::next);
    }

    public OutboundRequestIdSettingInterceptor(Supplier<String> nextRequestId) {
        this.nextRequestId = nextRequestId;
    }

    @Override
    public void process(HttpRequest request, HttpContext context) {
        request.setHeader(HttpHeaders.REQUEST_ID, nextRequestId.get());
        request.setHeader(HttpHeaders.ROOT_REQUEST_ID, MdcFields.getRootRequestId());
        request.setHeader(HttpHeaders.ORIGIN_REQUEST_ID, MdcFields.getRequestId());
    }

}
