package cinderellaadmin.vajaralabs.com.admin;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import common.Configg;

/**
 * Created by Karthik S on 8/24/2016.
 */
public class AdapterPickupedCustomer extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Toolbar toolbar;
    Button btn_get_sign, mClear, mGetSign, mCancel;

    File file2;
    Dialog dialog;
    LinearLayout mContent;
    View view;
    signature mSignature;
    Bitmap bitmap;
    File file;

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";

    private Context context;

    private ProgressDialog progressBar;
    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";
    private String currentDateTime = "";

    public AdapterPickupedCustomer(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
        this.hashMapArrayList = hashMapArrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return hashMapArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return hashMapArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return hashMapArrayList.size();
    }

    @Override
    public View getView(final int posistion, View convertView, ViewGroup viewGroup) {

        MyViewHolder mViewHolder;
//
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.pickuped_list, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.tag_no.setText(hashMapArrayList.get(posistion).get("tag_no"));
        mViewHolder.txt_unique_code.setText(hashMapArrayList.get(posistion).get("unique_code"));
        mViewHolder.txt_clothes.setText(hashMapArrayList.get(posistion).get("items"));
//        mViewHolder.txt_oveal_total.setText(hashMapArrayList.get(posistion).get("overall_total"));
        mViewHolder.txt_shop.setText(hashMapArrayList.get(posistion).get("shop_code_and_name"));
        mViewHolder.txt_shop_contact.setText(hashMapArrayList.get(posistion).get("shop_contact"));

        mViewHolder.txt_no_of_cloths.setText(hashMapArrayList.get(posistion).get("overall_count"));

        mViewHolder.txt_total_amt.setText(hashMapArrayList.get(posistion).get("overall_total"));
        mViewHolder.txt_payment_mode.setText(hashMapArrayList.get(posistion).get("payment_mode"));
        mViewHolder.txt_amt_taken.setText(hashMapArrayList.get(posistion).get("given_amt"));
        mViewHolder.txt_balance_amt.setText(hashMapArrayList.get(posistion).get("balance_amt"));
//        mViewHolder.txt_shop_contact.setText(hashMapArrayList.get(posistion).get("shop_contact"));


        if (Configg.getDATA(context, "type").equals("factory")) {
            if (hashMapArrayList.get(posistion).get("completion").equals("75")) {
                mViewHolder.txt_assign_factory.setText("Pending Swich to Complete");
            } else if (hashMapArrayList.get(posistion).get("completion").equals("90")) {
                mViewHolder.txt_assign_factory.setText("Job Completed ");

            }
        } else if (Configg.getDATA(context, "type").equals("shop")) {
            if (hashMapArrayList.get(posistion).get("completion").equals("90")) {
                mViewHolder.txt_assign_factory.setText("Assign This to Delivery Person " + hashMapArrayList.get(posistion).get("delivery_contact_name"));

            } else if (hashMapArrayList.get(posistion).get("completion").equals("95")) {
                mViewHolder.txt_assign_factory.setText("Assign This to Delivery Person " + hashMapArrayList.get(posistion).get("delivery_contact_name"));

            }
        } else if (Configg.getDATA(context, "type").equals("delivery")) {
            mViewHolder.linear_1.setVisibility(View.GONE);
            mViewHolder.btn_finish.setVisibility(View.VISIBLE);
        }
        mViewHolder.txt_assign_factory.setTag(hashMapArrayList.get(posistion));
//        mViewHolder.mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//
//                if (isChecked) {
//                    mViewHolder.mySwitch.setText("IN");
//                    volley_post_user(getIntent().getStringExtra("doctor_id"), "1");
//                } else {
//                    mViewHolder.mySwitch.setText("OUT");
//                    volley_post_user(getIntent().getStringExtra("doctor_id"), "0");
//
//                }
//
//            }
//        });
        mViewHolder.txt_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MoreView.class);
                intent.putExtra("overall_count", hashMapArrayList.get(posistion).get("overall_count"));
                intent.putExtra("given_amt", hashMapArrayList.get(posistion).get("given_amt"));
                intent.putExtra("balance_amt", hashMapArrayList.get(posistion).get("balance_amt"));
                intent.putExtra("overall_total", hashMapArrayList.get(posistion).get("overall_total"));
                intent.putExtra("image_one", hashMapArrayList.get(posistion).get("image_one"));
                intent.putExtra("items", hashMapArrayList.get(posistion).get("items"));

                context.startActivity(intent);
            }
        });
        mViewHolder.btn_finish.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {


          // Method to create Directory, if the Directory doesn't exists
    //                                                          file = new File(DIRECTORY);
    //                                                          if (!file.exists()) {
    //                                                              file.mkdir();
    //                                                          }
    //
    //                                                          // Dialog Function
    //                                                          dialog = new Dialog(context);
    //                                                          // Removing the features of Normal Dialogs
    //                                                          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //                                                          dialog.setContentView(R.layout.dialog_signature);
    //                                                          dialog.setCancelable(true);
    //                                                          dialog_action(posistion);


          Dialog dialog = new Dialog(context);
          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
          dialog.setContentView(R.layout.finish_customer);
          final EditText edt_amt = (EditText) dialog.findViewById(R.id.edt_amt);
          Button btn_finish = (Button) dialog.findViewById(R.id.btn_submit);
          final TextView txt_balance = (TextView) dialog.findViewById(R.id.txt_balance);
          final TextView txt_already = (TextView) dialog.findViewById(R.id.txt_given);
          String balance_amt;
          if (!hashMapArrayList.get(posistion).get("balance_amt").equals("")) {
              balance_amt = hashMapArrayList.get(posistion).get("balance_amt").replaceAll("[^\\d.]", "");

          } else {
              balance_amt = hashMapArrayList.get(posistion).get("overall_total") + "vcxv".replaceAll("[^\\d.]", "");
          }
          if (!hashMapArrayList.get(posistion).get("given_amt").equals("")) {
              txt_already.setText(hashMapArrayList.get(posistion).get("given_amt"));

          } else {
              txt_already.setText("0.0");

          }


          txt_balance.setText(balance_amt);
    //                final Double[] aDouble_taken = {0.0};
          edt_amt.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                 }

                 @Override
                 public void onTextChanged(CharSequence s, int start, int before, int count) {
                     String s1 = s.toString();
                     if (!s1.equals("")) {

                         txt_already.setText("");
                         txt_balance.setText("");
                         String balance_amt2;
                         if (!hashMapArrayList.get(posistion).get("balance_amt").equals("")) {
                             balance_amt2 = hashMapArrayList.get(posistion).get("balance_amt").replaceAll("[^\\d.]", "");
                         } else {
                             balance_amt2 = hashMapArrayList.get(posistion).get("overall_total").replaceAll("[^\\d.]", "");

                         }

                         String final_amt = edt_amt.getText().toString();
                         Double aDouble_balance = Double.parseDouble(balance_amt2) - Double.parseDouble(final_amt);

                         Double aDouble_taken;
                         if (!hashMapArrayList.get(posistion).get("given_amt").equals("")) {

                             aDouble_taken = Double.parseDouble(hashMapArrayList
                                     .get(posistion).get("given_amt")) + Double.parseDouble(edt_amt.getText().toString());

                         } else {
                             aDouble_taken = Double.parseDouble("0.0") + Double.parseDouble(edt_amt.getText().toString());

                         }
                         if (aDouble_balance > -1) {
                             txt_already.setText(String.valueOf(aDouble_taken));
                             txt_balance.setText(String.valueOf(aDouble_balance));
                         } else {
                             Toast.makeText(context, "Entered Maximum Amt.", Toast.LENGTH_SHORT).show();
                         }

                     } else

                     {
                         String balance_amt = hashMapArrayList.get(posistion).get("balance_amt").replaceAll("[^\\d.]", "");

                         txt_balance.setText(balance_amt);
                         txt_already.setText(hashMapArrayList.get(posistion).get("given_amt"));
                     }

                 }

                 @Override
                 public void afterTextChanged(Editable s) {

                 }
             }

          );
      btn_finish.setOnClickListener(new View.OnClickListener()

                                        {
                                            @Override
                                            public void onClick(View view) {

            System.out.println();
            if (!edt_amt.getText().toString().equals("")) {
                String balance_amt3 = "";
                if (!hashMapArrayList.get(posistion).get("balance_amt").equals("")) {

                    balance_amt3 = hashMapArrayList.get(posistion).get("balance_amt").replaceAll("[^\\d.]", "");
                } else {
                    balance_amt3 = hashMapArrayList.get(posistion).get("overall_total").replaceAll("[^\\d.]", "");

                }

                String final_amt = edt_amt.getText().toString();
                Double aDouble_balance = Double.parseDouble(balance_amt3) - Double.parseDouble(final_amt);
//                                                                                                    Double aDouble_taken = Double.parseDouble(hashMapArrayList
//                                                                                                            .get(posistion).get("given_amt")) + Double.parseDouble(edt_amt.getText().toString());
                if (aDouble_balance > -1) {
                    volley_finish(hashMapArrayList.get(posistion).get("pcid"),
                            "100",
                            hashMapArrayList.get(posistion).get("tag_no"),
                            txt_already.getText().toString(),
                            txt_balance.getText().toString(),
                            Configg.getDATA(context, "did"),
                            Configg.getDATA(context, "delivery_person"), posistion);
                } else {
                    Toast.makeText(context, "Enter The Valid Amount.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Enter The Amount.", Toast.LENGTH_SHORT).show();
            }
        }
    }

          );

          if (!hashMapArrayList.get(posistion).get("given_amt").equals("0.0")) {
              dialog.show();

          } else {
              Dialog dialog2 = new Dialog(context);
              dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
              dialog2.setContentView(R.layout.finish_customer2);
              final EditText edt_amt2 = (EditText) dialog2.findViewById(R.id.edt_amt);
              final TextView txt_balance2 = (TextView) dialog2.findViewById(R.id.txt_balance);
              final TextView txt_already2 = (TextView) dialog2.findViewById(R.id.txt_given);
              Button btn_finish2 = (Button) dialog2.findViewById(R.id.btn_submit);
              btn_finish2.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      //                                                                      if (Double.parseDouble())
                      if (!edt_amt2.getText().toString().equals("")) {
                          Double aDouble1 = Double.parseDouble(hashMapArrayList.get(posistion).get("overall_total"));
                          Double aDouble2 = Double.parseDouble(edt_amt2.getText().toString());
                          if (!(aDouble2 > aDouble1)) {
                              if (Double.parseDouble(txt_already2.getText().toString()) > 50) {
                                  volley_finish(hashMapArrayList.get(posistion).get("pcid"),
                                          "100",
                                          hashMapArrayList.get(posistion).get("tag_no"),
                                          txt_already2.getText().toString(),
                                          txt_balance2.getText().toString(),
                                          Configg.getDATA(context, "did"),
                                          Configg.getDATA(context, "delivery_person"), posistion);
                              } else {
                                  Toast.makeText(context, "Enter Valid Amount.", Toast.LENGTH_SHORT).show();
                              }
                          } else {
                              Toast.makeText(context, "Please Enter The Valid Amt.", Toast.LENGTH_SHORT).show();
                          }

                      } else {
                          Toast.makeText(context, "Please Enter The Valid Amt.", Toast.LENGTH_SHORT).show();
                      }
                  }
              });

              edt_amt2.addTextChangedListener(new TextWatcher() {
                  @Override
                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                  }

                  @Override
                  public void onTextChanged(CharSequence s, int start, int before, int count) {

                      String s1 = s.toString();
                      if (!s1.equals("")) {
                          Double aDouble1 = Double.parseDouble(hashMapArrayList.get(posistion).get("overall_total"));
                          Double aDouble2 = Double.parseDouble(s1);
                          Double bal = aDouble1 - aDouble2;

                          txt_already2.setText(aDouble2 + "");
                          txt_balance2.setText(bal + "");

                          if (aDouble2 > aDouble1) {
                              Toast.makeText(context, "Enter Valid Amt.", Toast.LENGTH_SHORT).show();
                          }


                      } else {
                          txt_already2.setText("0.0");
                          txt_balance2.setText("0.0");
                      }


                  }

                  @Override
                  public void afterTextChanged(Editable s) {

                  }
              });
              dialog2.show();
          }
      }
    }

    );
    mViewHolder.txt_assign_factory.setOnClickListener(new View.OnClickListener()

          {
              @Override
              public void onClick(View v) {
                  if (Configg.getDATA(context, "type").equals("shop")) {

                      if (hashMapArrayList.get(posistion).get("completion").equals("90")
                              || hashMapArrayList.get(posistion).get("completion").equals("95")) {
                          final Dialog dialog = new Dialog(context);
                          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                          dialog.setContentView(R.layout.confirmation2);
                          Button button = (Button) dialog.findViewById(R.id.btn_yes);
                          Button button1 = (Button) dialog.findViewById(R.id.btn_no);
                          button.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  volley_post_job_done(hashMapArrayList.get(posistion).get("pcid"), "95", hashMapArrayList.get(posistion).get("delivery_id"),
                                          hashMapArrayList.get(posistion).get("delivery_contact_name"),
                                          hashMapArrayList.get(posistion).get("delivery_contact"));
                                  dialog.dismiss();

                              }
                          });
                          button1.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  Intent intent = new Intent(context, AssignDealer.class);
                                  intent.putExtra("completion", "90");
                                  intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));

                                  context.startActivity(intent);
                                  dialog.dismiss();
                                  ((Activity) context).finish();
                              }
                          });
                          dialog.show();
                      } else {
                          Intent intent = new Intent(context, FactoryList.class);
                          ((AssignFactory) context).shashMapArrayList = (HashMap<String, String>) v.getTag();
                          intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                          intent.putExtra("tag_no", hashMapArrayList.get(posistion).get("tag_no"));
                          ((AssignFactory) context).startActivityForResult(intent, 1111);
                      }

                  } else if (Configg.getDATA(context, "type").equals("factory")) {
                      if (hashMapArrayList.get(posistion).get("completion").equals("75")) {
                          volley_post_job_done(hashMapArrayList.get(posistion).get("pcid"), "90", "", "", "");
                      } else if (hashMapArrayList.get(posistion).get("completion").equals("90")) {
                          volley_post_job_done(hashMapArrayList.get(posistion).get("pcid"), "75", "", "", "");
                      }
                  }

              }
          }

        );

//        }
        return convertView;
    }


    public void dialog_action(final int posistion) {

        mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        TextView txt_unique_code = (TextView) dialog.findViewById(R.id.txt_unique_code);
        TextView txt_no_of_cloths = (TextView) dialog.findViewById(R.id.txt_no_of_cloths);
        TextView txt_clothes = (TextView) dialog.findViewById(R.id.txt_clothes);
        TextView txt_payed = (TextView) dialog.findViewById(R.id.txt_payed);
        TextView txt_balance_amt = (TextView) dialog.findViewById(R.id.txt_balance_amt);
        txt_unique_code.setText(hashMapArrayList.get(posistion).get("unique_code"));
        txt_no_of_cloths.setText(hashMapArrayList.get(posistion).get("overall_count"));
        txt_clothes.setText(hashMapArrayList.get(posistion).get("items"));
        txt_payed.setText(hashMapArrayList.get(posistion).get("given_amt"));
        txt_balance_amt.setText(hashMapArrayList.get(posistion).get("balance_amt"));

//        TextView txt_unique_code=(TextView)dialog.findViewById(R.id.txt_unique_code);

        mSignature = new signature(context, null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = (Button) dialog.findViewById(R.id.cancel);
        view = mContent;

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });
        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Log.v("tag", "Panel Saved");
                view.setDrawingCacheEnabled(true);
                mSignature.save(view, StoredPath);
                dialog.dismiss();
                Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show();
                UploadFileToServer uploadFileToServer = new UploadFileToServer(posistion);
                uploadFileToServer.execute();

            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("tag", "Panel Cancelled");
                dialog.dismiss();
                // Calling the same class
//                ((Activity)context).recreate();
            }
        });
        dialog.show();
    }

    private class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String StoredPath) {
            Log.v("tag", "Width: " + v.getWidth());
            Log.v("tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);
                // Convert the output file to Image such as .png
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();

                System.out.println("path" + StoredPath);
                file2 = new File(StoredPath);
            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:
                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;
                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
            Log.v("log_tag", string);
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

    private void volley_post_job_done(final String pcid, final String completion,
                                      final String id,
                                      final String name,
                                      final String mobile) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Configg.MAIN_URL + Configg.FACTORY_JOB_DONE, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
                        if (completion.equals("90"))
                            Configg.alert("JOB Completion", "Completed Successfully", 2, context);
                        else
                            Configg.alert("JOB Completion", "Pending", 2, context);

                    } else if (jsonObject.getString("success").equals("3")) {
                        Configg.alert("JOB Completion", jsonObject.getString("message"), 0, context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(getApplicationContext(), "response" + response, 1000).show();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                pDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pcid", pcid);
                params.put("delivery_assign_id", id);
                params.put("completion", completion);
                params.put("delivery_contact_name", name);
                params.put("delivery_contact", mobile);
                if (Configg.getDATA(context, "type").equals("factory")) {
                    if (completion.equals("90")){
                        params.put("FactoryJobDone", "true");
                    }
                    params.put("sms", "true");
                } else {
                    params.put("sms", "false");
                }

//                params.put("comment", Uri.encode(comment));
//                params.put("comment_post_ID",String.valueOf(postId));
//                params.put("blogId",String.valueOf(blogId));
                System.out.println("params" + params.toString());

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        int i;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            progressBar = new ProgressDialog(context);
            progressBar.show();
            progressBar.setCancelable(false);
            progressBar.setMessage("Loading...");

        }

        UploadFileToServer(int i) {
            this.i = i;

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
//            progressBar.setVisibility(View.VISIBLE);
//
//            // updating progress bar value
//            progressBar.setProgress(progress[0]);

            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Configg.MAIN_URL + Configg.BILL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

//                File sourceFile = new File(destination);

                // Adding file data to http body
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                currentDateTime = dateFormat.format(new Date()); // Find todays dat

                entity.addPart("image", new FileBody(file2));
                entity.addPart("pcid", new StringBody(hashMapArrayList.get(i).get("pcid")));
                entity.addPart("completed_id", new StringBody(Configg.getDATA(context, "did")));
                entity.addPart("completed_person", new StringBody(Configg.getDATA(context, "delivery_person")));
                entity.addPart("completed_date", new StringBody(currentDateTime));

//
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            Log.e(TAG, "Response from server: " + result);

            System.out.println("Response from server: " + result);

            progressBar.dismiss();
            // showing the server response in an alert dialog


            Configg.alert("Response From Server...", "Posted Successfully.", 2, context);


        }

    }


    private class MyViewHolder {
        private final TextView tag_no;
        private final TextView txt_unique_code;
        private final TextView txt_more, txt_clothes, txt_assign_factory;
        private final TextView txt_shop_contact, txt_shop, txt_total_amt;
        private final TextView txt_no_of_cloths;
        private final TextView txt_payment_mode;
        private final TextView txt_amt_taken;
        private final TextView txt_balance_amt;
        private LinearLayout linear_1;
        private final Button btn_finish;

        public MyViewHolder(View item) {
            txt_unique_code = (TextView) item.findViewById(R.id.txt_unique_code);
            txt_more = (TextView) item.findViewById(R.id.txt_more);
            tag_no = (TextView) item.findViewById(R.id.tag_no);
            txt_clothes = (TextView) item.findViewById(R.id.txt_clothes);
//            txt_oveal_total = (TextView) item.findViewById(R.id.txt_oveal_total);
            txt_assign_factory = (TextView) item.findViewById(R.id.txt_assign_factory);
            txt_shop_contact = (TextView) item.findViewById(R.id.txt_shop_contact);
            txt_shop = (TextView) item.findViewById(R.id.txt_shop);

            txt_no_of_cloths = (TextView) item.findViewById(R.id.txt_no_of_cloths);
            txt_total_amt = (TextView) item.findViewById(R.id.txt_total_amt);
            txt_payment_mode = (TextView) item.findViewById(R.id.txt_payment_mode);
            txt_amt_taken = (TextView) item.findViewById(R.id.txt_amt_taken);

            txt_balance_amt = (TextView) item.findViewById(R.id.txt_balance_amt);


            btn_finish = (Button) item.findViewById(R.id.btn_finish);
            linear_1 = (LinearLayout) item.findViewById(R.id.linear_1);

        }

    }

    private void volley_finish(final String pcid,
                               final String completion,
                               final String tag_no,
                               final String amt,
                               final String balance_amt,
                               final String delivery_id,
                               final String delivery_person,
                               final int pos) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Configg.MAIN_URL + Configg.FINAL_JOB_DONE, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
//                        ((Activity) context).finish();
                        file = new File(DIRECTORY);
                        if (!file.exists()) {
                            file.mkdir();
                        }

                        // Dialog Function
                        dialog = new Dialog(context);
                        // Removing the features of Normal Dialogs
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_signature);
                        dialog.setCancelable(false);
                        dialog_action(pos);

                    } else if (jsonObject.getString("success").equals("3")) {
//                        Configg.alert("JOB Completion", jsonObject.getString("message") + '\n' + "Amount Received : " +
//                                jsonObject.getString("amount") + '\n' + jsonObject.getString("tag_no") + " : " +
//                                jsonObject.getString("status"), 0, context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(getApplicationContext(), "response" + response, 1000).show();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                pDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pcid", pcid);
                params.put("completion", completion);
                params.put("tag_no", tag_no);
                params.put("given_amt", amt);
                params.put("balance_amt", balance_amt);
                params.put("delivery_time_id", delivery_id);
                params.put("delivery_time_person", delivery_person);


//                params.put("comment", Uri.encode(comment));
//                params.put("comment_post_ID",String.valueOf(postId));
//                params.put("blogId",String.valueOf(blogId));
                System.out.println("params" + params.toString());

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }


}
