package numa;

import io.numaproj.numaflow.function.FunctionServer;
import io.numaproj.numaflow.function.Message;
import io.numaproj.numaflow.function.metadata.Metadata;
import io.numaproj.numaflow.function.reduce.ReduceDatumStream;
import io.numaproj.numaflow.function.reduce.ReduceFunc;
import io.numaproj.numaflow.function.v1.Udfunction;

import java.io.IOException;

public class SimpleReduce {

    public static Message[] process(String key, ReduceDatumStream reduceDatumStream, Metadata md) {
        int sum = 0;
        String resultKey = key;
        while (true) {
            Udfunction.Datum datum = reduceDatumStream.ReadMessage();
            // null indicates the end of the input
            if (datum == ReduceDatumStream.EOF) {
                break;
            }
            try {
                sum += Integer.parseInt(new String(datum.getValue().toByteArray()));
            } catch (NumberFormatException e) {
            }
        }
        return new Message[]{Message.to(resultKey, String.valueOf(sum).getBytes())};
    }

    public static void main(String[] args) throws IOException {
        System.out.println("reduce was executed");
        (new FunctionServer()).registerReducer(new ReduceFunc(SimpleReduce::process)).start();
    }
}
