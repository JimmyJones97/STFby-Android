package com.xzy.forestSystem.baidu.speech;

import com.xzy.forestSystem.baidu.voicerecognition.android.Candidate;
import com.xzy.forestSystem.baidu.voicerecognition.android.Candidate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

class Results {
    Results() {
    }

    public static class Result {
        protected final HashMap<String, Object> bundle = new HashMap<>();

        /* renamed from: id */
        protected int f202id = 0;
        protected final JSONObject response;
        protected String strResponse;

        protected Result(JSONObject response2) throws Exception {
            this.response = response2;
            this.strResponse = response2.toString(4);
            this.bundle.put("origin_result", this.strResponse);
        }

        /* access modifiers changed from: package-private */
        public final int getInt(String key) {
            return this.response.optInt(key, 0);
        }

        /* access modifiers changed from: package-private */
        public final String getString(String key) {
            return this.response.optString(key, null);
        }

        public final HashMap<String, Object> toBundle() {
            return this.bundle;
        }

        public final int getId() {
            return this.f202id;
        }

        public final void setId(int id) {
            this.f202id = id;
        }

        public Result addPrefix(String prefix) throws Exception {
            if (prefix != null && !"".equals(prefix)) {
                Object tmp2 = this.bundle.get("results_recognition");
                if ((tmp2 instanceof ArrayList) && ((ArrayList) tmp2).size() > 0 && (((ArrayList) tmp2).get(0) instanceof String)) {
                    ArrayList<String> mergedList = new ArrayList<>();
                    for (int i = 0; i < ((ArrayList) tmp2).size(); i++) {
                        mergedList.add(prefix + ((ArrayList) tmp2).get(i));
                    }
                    this.bundle.put("results_recognition", mergedList);
                }
            }
            return this;
        }

        public String toString() {
            return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + ": " + this.response;
        }
    }

    public static final class StubResult extends Result {
        public StubResult(JSONObject response) throws Exception {
            super(response);
        }
    }

    public static class RunningResult extends Result {
        private Object resultForSdk1X;

        public RunningResult(JSONObject response) throws Exception {
            super(response);
            JSONObject jsonContent = response.getJSONObject("content");
            ArrayList<String> resultList = new ArrayList<>();
            JSONArray tmp = jsonContent.getJSONArray("item");
            for (int j = 0; j < tmp.length(); j++) {
                resultList.add(tmp.getString(j));
            }
            this.resultForSdk1X = resultList;
            this.bundle.put("results_recognition", resultList);
        }

        @Override // com.baidu.speech.Results.Result
        public Result addPrefix(String prefix) throws Exception {
            super.addPrefix(prefix);
            if (prefix != null && !"".equals(prefix)) {
                JSONArray oldItems = this.response.getJSONObject("content").getJSONArray("item");
                JSONArray prefixedItems = new JSONArray();
                for (int i = 0; i < oldItems.length(); i++) {
                    prefixedItems.put(prefix + oldItems.getString(i));
                }
                this.response.getJSONObject("content").put("item", prefixedItems);
                this.strResponse = this.response.toString(4);
                this.bundle.put("origin_result", this.strResponse);
            }
            return this;
        }
    }

    public static class SentenceEndResult extends RunningResult {
        public SentenceEndResult(JSONObject response) throws Exception {
            super(response);
        }
    }

    public static final class FinalResult extends Result {
        private Object resultForSdk1X;

        public Object getResultForSdk1x() {
            return this.resultForSdk1X;
        }

        public FinalResult(JSONObject response) throws Exception {
            super(response);
            try {
                if (response.has("idxs")) {
                    ArrayList<List<Candidate>> resultList = new ArrayList<>();
                    JSONArray arr = response.getJSONArray("idxs");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONArray contentArr = arr.getJSONObject(i).getJSONArray("content");
                        for (int j = 0; j < contentArr.length(); j++) {
                            List<Candidate> subList = new ArrayList<>();
                            JSONObject tmp = contentArr.getJSONArray(j).getJSONObject(0);
                            Iterator<String> iterator = tmp.keys();
                            while (iterator.hasNext()) {
                                String k = iterator.next();
                                subList.add(new Candidate(k, tmp.getDouble(k)));
                            }
                            resultList.add(subList);
                        }
                    }
                    this.resultForSdk1X = resultList;
                    ArrayList<String> basicList = new ArrayList<>();
                    StringBuilder sb = new StringBuilder();
                    Iterator i$ = resultList.iterator();
                    while (i$.hasNext()) {
//                        sb.append(i$.next().get(0).getWord());
                    }
                    basicList.add(sb.toString());
                    this.bundle.put("results_recognition", basicList);
                    return;
                }
                JSONObject jsonContent = response.getJSONObject("content");
                ArrayList<String> resultList2 = new ArrayList<>();
                JSONArray tmp2 = jsonContent.getJSONArray("item");
                for (int j2 = 0; j2 < tmp2.length(); j2++) {
                    resultList2.add(tmp2.getString(j2));
                }
                this.resultForSdk1X = resultList2;
                this.bundle.put("results_recognition", resultList2);
            } catch (Exception e) {
                throw new Exception("#5, Other client side errors.," + response + "", e);
            }
        }

        @Override // com.baidu.speech.Results.Result
        public Result addPrefix(String prefix) throws Exception {
            super.addPrefix(prefix);
            if (prefix != null && !"".equals(prefix)) {
                if (this.response.has("idxs")) {
                    JSONArray arr = this.response.getJSONArray("idxs");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONArray contentArr = arr.getJSONObject(i).getJSONArray("content");
                        for (int j = contentArr.length() - 1; j >= 0; j--) {
                            contentArr.put(j + 1, contentArr.getJSONArray(j));
                        }
                        contentArr.put(0, new JSONArray().put(new JSONObject().put(prefix, 1.0d)));
                    }
                    this.strResponse = this.response.toString(4);
                    this.bundle.put("origin_result", this.strResponse);
                } else {
                    JSONArray oldItems = this.response.getJSONObject("content").getJSONArray("item");
                    JSONArray prefixedItems = new JSONArray();
                    for (int i2 = 0; i2 < oldItems.length(); i2++) {
                        prefixedItems.put(prefix + oldItems.getString(i2));
                    }
                    this.response.getJSONObject("content").put("item", prefixedItems);
                    this.strResponse = this.response.toString(4);
                    this.bundle.put("origin_result", this.strResponse);
                }
            }
            return this;
        }

        public boolean success() {
            return getInt("err_no") == 0;
        }

        public int err_no() {
            return getInt("err_no");
        }
    }
}
