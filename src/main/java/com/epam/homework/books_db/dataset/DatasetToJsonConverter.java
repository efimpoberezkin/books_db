package com.epam.homework.books_db.dataset;

import com.google.gson.Gson;

public class DatasetToJsonConverter {

    public String toJson(Dataset dataset) {
        Gson gson = new Gson();
        return gson.toJson(dataset);
    }
}
