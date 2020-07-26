package com.equifax.ews.dataflow;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class AvroCsvDataflow {

    public interface DFPipelineOptions extends DataflowPipelineOptions {

        @Validation.Required
        @Default.String("gs://aruncloudspace/BIGFILE/decryptedAvrofileNew.avro")
        String getInputFile();
        void setInputFile(String value);


        @Validation.Required
        @Default.String("gs://aruncloudspace/BIGFILE/decryptedAvrofileNew.txt")
        String getOutput();
        void setOutput(String value);


        @Validation.Required
        @Default.String("gs://ews-de-twn-cnfg-qa/schema/newschema/INQUIRY_TWNBOC.avsc")
        String getAvroSchema();
        void setAvroSchema(String value);

        @Default.String(",")
        String getCsvDelimiter();
        void setCsvDelimiter(String delimiter);

        @Default.String("1")
        Integer getNumShards();
        void setNumShards(Integer delimiter);

    }

    public static void main(String[] args) throws IOException {
        PipelineOptionsFactory.register(DFPipelineOptions.class);
        DFPipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(DFPipelineOptions.class);
        runAvroToCsv(options);
    }

    public static void checkFieldTypes(Schema schema) throws IllegalArgumentException {
        for (Schema.Field field : schema.getFields()) {
            String fieldType = field.schema().getType().getName().toLowerCase();

        }
    }

    private static String getSchema(String schemaPath) throws IOException {
        ReadableByteChannel channel = FileSystems.open(FileSystems.matchNewResource(
                schemaPath, false));

        try (InputStream stream = Channels.newInputStream(channel)) {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder dataBuilder = new StringBuilder();

            String line;
            while ((line = streamReader.readLine()) != null) {
                dataBuilder.append(line);
            }

            return dataBuilder.toString();
        }
    }


    public static void runAvroToCsv(DFPipelineOptions options)
            throws IOException, IllegalArgumentException {
        FileSystems.setDefaultPipelineOptions(options);

        // Get Avro Schema
        String schemaJson = getSchema(options.getAvroSchema());
        Schema schema = new Schema.Parser().parse(schemaJson);

        // Check schema field types before starting the Dataflow job
        checkFieldTypes(schema);

        // Create the Pipeline object with the options we defined above.
        Pipeline pipeline = Pipeline.create(options);

        // Convert Avro To CSV
        pipeline.apply("Read Avro files",
                AvroIO.readGenericRecords(schemaJson).from(options.getInputFile()))
                .apply("Convert Avro to CSV formatted data",
                        ParDo.of(new ConvertAvroToCsv(schemaJson, options.getCsvDelimiter())))
                .apply("Write CSV formatted data",
                        TextIO.write().withWindowedWrites()
                .withNumShards(options.getNumShards()).to(options.getOutput())
                        .withSuffix(".txt"));

        // Run the pipeline.
        pipeline.run().waitUntilFinish();
    }

    public static class ConvertAvroToCsv extends DoFn<GenericRecord, String> {

        private String delimiter;
        private String schemaJson;

        public ConvertAvroToCsv(String schemaJson, String delimiter) {
            this.schemaJson = schemaJson;
            this.delimiter = delimiter;
        }

        @ProcessElement
        public void processElement(ProcessContext ctx) {
            GenericRecord genericRecord = ctx.element();
            Schema schema = new Schema.Parser().parse(schemaJson);

            StringBuilder row = new StringBuilder();
            for (Schema.Field field : schema.getFields()) {
                String fieldType = field.schema().getType().toString().toLowerCase();

                if (row.length() > 0) {
                    row.append(delimiter);
                }

                row.append(genericRecord.get(field.name()));
            }
            ctx.output(row.toString());
        }
    }
}