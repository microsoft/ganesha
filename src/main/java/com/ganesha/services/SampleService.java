package com.ganesha.services;

// demonstrating builder pattern for handling default values in Java.
public class SampleService {
    private final String _name; // but instead make it a client from an SDK or something
    private final String _someProp;

    public SampleService(SampleServiceBuilder builder) {
        // create service
        _name = builder.name;
        _someProp = builder.someProp;
    }

    public String getName() {
        return _name;
    }

    public String getSomeProp() {
        return _someProp;
    }

    public static class SampleServiceBuilder {
        private String name = "SampleService";
        private String someProp = "MyPropValue";

        public SampleServiceBuilder setName(String firstName) {
            this.name = firstName;
            return this;
        }

        public SampleServiceBuilder setSomeProp(String someProp) {
            this.someProp = someProp;
            return this;
        }

        public SampleService build() {
            return new SampleService(this);
        }
    }
}
