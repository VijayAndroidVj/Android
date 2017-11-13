package cinderellaadmin.vajaralabs.com.admin;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
public class AdapterBooking extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = null;
    ArrayList<HashMap<String, String>> hashMapArrayList2;


    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterBooking(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
        this.hashMapArrayList = hashMapArrayList;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.hashMapArrayList2 = new ArrayList<HashMap<String, String>>();
        this.hashMapArrayList2.addAll(hashMapArrayList);

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

            convertView = inflater.inflate(R.layout.booking_list, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.txt_date.setText(hashMapArrayList.get(posistion).get("pickup_date"));
        String count = hashMapArrayList.get(posistion).get("overall_count");
        if (TextUtils.isEmpty(count)) {
            count = "";
        } else {
            count = "-" + count;
        }
        mViewHolder.txt_unique_code.setText(hashMapArrayList.get(posistion).get("unique_code") + count);
        mViewHolder.txt_time.setText(hashMapArrayList.get(posistion).get("pickup_time").trim());
        mViewHolder.txt_city.setText(hashMapArrayList.get(posistion).get("city"));
        mViewHolder.txt_locality.setText(hashMapArrayList.get(posistion).get("locality"));
        mViewHolder.txt_address.setText(hashMapArrayList.get(posistion).get("address"));
        mViewHolder.txt_mobile.setText(hashMapArrayList.get(posistion).get("mobile"));
        mViewHolder.txt_name.setText(hashMapArrayList.get(posistion).get("customer_name").trim());

        String totalAmount = hashMapArrayList.get(posistion).get("overall_total");
        if (android.text.TextUtils.isEmpty(totalAmount)) {
            mViewHolder.lltotal_amt.setVisibility(View.GONE);
        } else {
            mViewHolder.lltotal_amt.setVisibility(View.VISIBLE);
            mViewHolder.txt_amount.setText(totalAmount);
        }
        if (hashMapArrayList.get(posistion).get("completion").equals("25")) {
            mViewHolder.txt_cancel.setVisibility(View.VISIBLE);

        } else if (hashMapArrayList.get(posistion).get("completion").equals("")) {
            mViewHolder.txt_cancel.setVisibility(View.VISIBLE);
            mViewHolder.lltotal_amt.setVisibility(View.GONE);

        }

        if (Configg.getDATA(context, "type").equals("shop")) {
            if (hashMapArrayList.get(posistion).get("completion").equals("25")) {
                mViewHolder.txt_assign.setText("Assigned To Delivery Person " + hashMapArrayList.get(posistion).get("delivery_contact_name"));
            } else if (hashMapArrayList.get(posistion).get("completion").equals("50")) {
                mViewHolder.txt_assign.setText("Order-Pickuped BY " + hashMapArrayList.get(posistion).get("delivery_contact_name"));
            } else if (hashMapArrayList.get(posistion).get("completion").equals("75")) {
                mViewHolder.txt_assign.setText("Assigned To Factory " + hashMapArrayList.get(posistion).get("factory_name"));
            } else if (hashMapArrayList.get(posistion).get("completion").equals("90")) {
                mViewHolder.txt_assign.setText("Job Completed By  " + hashMapArrayList.get(posistion).get("factory_name"));
            } else if (hashMapArrayList.get(posistion).get("completion").equals("95")) {
                mViewHolder.txt_assign.setText("Job Completed 95% ");


            } else if (hashMapArrayList.get(posistion).get("completion").equals("")) {
                mViewHolder.txt_assign.setText("Assign To Delivery Person  ");


            }
        } else if (Configg.getDATA(context, "type").equals("delivery")) {

            if (hashMapArrayList.get(posistion).get("completion").equals("25")) {

                mViewHolder.txt_assign.setText("Pickup-Order");
            } else if (hashMapArrayList.get(posistion).get("completion").equals("50")) {
//                Toast.makeText(context,"deliver",1000).show();
                mViewHolder.txt_assign.setText("Order-Placed");
            } else if (hashMapArrayList.get(posistion).get("completion").equals("75")) {
                mViewHolder.txt_assign.setText("Assigned To Factory " + hashMapArrayList.get(posistion).get("factory_name"));
            } else if (hashMapArrayList.get(posistion).get("completion").equals("95")) {
                mViewHolder.txt_assign.setText("Job Completion 95% Delivery to Home");
            }


        } else if (Configg.getDATA(context, "type").equals("factory")) {
            if (hashMapArrayList.get(posistion).get("completion").equals("75")) {
                mViewHolder.txt_assign.setText("View Detail-" + hashMapArrayList.get(posistion).get("shop_code_and_name"));
            } else if (hashMapArrayList.get(posistion).get("completion").equals("90")) {
                mViewHolder.txt_assign.setText("View Detail-" + hashMapArrayList.get(posistion).get("shop_code_and_name"));

            } else if (hashMapArrayList.get(posistion).get("completion").equals("95")) {
                mViewHolder.txt_assign.setText("95% completed and assigned to Delivery Person" + hashMapArrayList.get(posistion).get("delivery_contact_name"));

            }
        } else if (Configg.getDATA(context, "type").equals("admin")) {
            if (hashMapArrayList.get(posistion).get("completion").equals("25")) {
                mViewHolder.txt_assign.setText("Assigned To Delivery Person " + hashMapArrayList.get(posistion).get("delivery_contact_name"));
            } else if (hashMapArrayList.get(posistion).get("completion").equals("50")) {
                mViewHolder.txt_assign.setText("Order-Pickuped BY " + hashMapArrayList.get(posistion).get("delivery_contact_name"));
            } else if (hashMapArrayList.get(posistion).get("completion").equals("75")) {
                mViewHolder.txt_assign.setText("Assigned To Factory " + hashMapArrayList.get(posistion).get("factory_name"));
            } else if (hashMapArrayList.get(posistion).get("completion").equals("90")) {
                mViewHolder.txt_assign.setText("Job Completed By  " + hashMapArrayList.get(posistion).get("factory_name"));
            } else if (hashMapArrayList.get(posistion).get("completion").equals("95")) {
                mViewHolder.txt_assign.setText("Job Completed 95% ");


            } else if (hashMapArrayList.get(posistion).get("completion").equals("")) {
                mViewHolder.txt_assign.setVisibility(View.INVISIBLE);
                mViewHolder.txt_assign.setText("Assign To Delivery Person  ");


            }
        }
        mViewHolder.txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.confirmation_cancel);
                final EditText edt_reason = (EditText) dialog.findViewById(R.id.edt_reason);
                Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
                Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!edt_reason.getText().toString().equals("")) {

                            volley_cancel_booking("yes" + "," +
                                    edt_reason.getText().toString(), hashMapArrayList.get(posistion).get("pcid"));

                        } else {
                            Toast.makeText(context, "Give The Valid Reason.", Toast.LENGTH_SHORT).show();
                        }

//


                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
//


            }
        });
        mViewHolder.txt_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    AssignFactory.saddresshashMapArrayList = hashMapArrayList.get(posistion);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Configg.getDATA(context, "type").equals("shop")) {

                    if (hashMapArrayList.get(posistion).get("completion").equals("50")) {
                        Intent intent = new Intent(context, AssignFactory.class);
                        intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                        context.startActivity(intent);


                    } else if (hashMapArrayList.get(posistion).get("completion").equals("25")) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.status_popup);
                        TextView txt_completion = (TextView) dialog.findViewById(R.id.txt_completion);
                        TextView txt_status = (TextView) dialog.findViewById(R.id.txt_status);
                        TextView txt_name = (TextView) dialog.findViewById(R.id.txt_name);
                        TextView txt_mobile = (TextView) dialog.findViewById(R.id.txt_mobile);
                        txt_mobile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        Button btn_reschedule = (Button) dialog.findViewById(R.id.btn_reschedule);
                        btn_reschedule.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, AssignDealer.class);
                                intent.putExtra("shop_id", Configg.getDATA(context, "sid"));
                                intent.putExtra("shop_keeper", Configg.getDATA(context, "shop_keeper"));
                                intent.putExtra("mobile", Configg.getDATA(context, "mobile"));

                                intent.putExtra("shop_code_and_name", Configg.getDATA(context, "shop_code_and_name"));
                                intent.putExtra("completion", hashMapArrayList.get(posistion).get("completion"));

                                intent.putExtra("delivery_contact_name", hashMapArrayList.get(posistion).get("delivery_contact_name"));


                                intent.putExtra("city", hashMapArrayList.get(posistion).get("city"));

                                intent.putExtra("locality", hashMapArrayList.get(posistion).get("locality"));
                                intent.putExtra("customer_id", hashMapArrayList.get(posistion).get("customer_id"));
                                intent.putExtra("mobile", hashMapArrayList.get(posistion).get("mobile"));
                                intent.putExtra("pickup_date", hashMapArrayList.get(posistion).get("pickup_date"));
                                intent.putExtra("pickup_time", hashMapArrayList.get(posistion).get("pickup_time"));
                                intent.putExtra("mobile_date", hashMapArrayList.get(posistion).get("mobile_date"));
                                intent.putExtra("city_id", hashMapArrayList.get(posistion).get("city_id"));
                                intent.putExtra("locality_id", hashMapArrayList.get(posistion).get("locality_id"));
                                intent.putExtra("delivery_id", hashMapArrayList.get(posistion).get("delivery_id"));
                                intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                                intent.putExtra("delivery_person", hashMapArrayList.get(posistion).get("delivery_person"));
                                intent.putExtra("address", hashMapArrayList.get(posistion).get("address"));
                                intent.putExtra("unique_code", hashMapArrayList.get(posistion).get("unique_code"));
                                dialog.dismiss();


                                context.startActivity(intent);
                            }
                        });

                        txt_completion.setText("25%");
                        txt_status.setText("Booking Code " + hashMapArrayList.get(posistion).get("unique_code") + '\n' + "Assigned To" +
                                hashMapArrayList.get(posistion).get("delivery_contact_name") + '\n' + "And Not Pickuped Yet.");
                        txt_name.setText(hashMapArrayList.get(posistion).get("delivery_contact_name"));
                        txt_mobile.setText(hashMapArrayList.get(posistion).get("delivery_contact"));
                        dialog.show();


                    } else if (hashMapArrayList.get(posistion).get("completion").equals("75")) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.status_popup);
                        TextView txt_completion = (TextView) dialog.findViewById(R.id.txt_completion);
                        TextView txt_status = (TextView) dialog.findViewById(R.id.txt_status);
                        TextView txt_name = (TextView) dialog.findViewById(R.id.txt_name);
                        TextView txt_mobile = (TextView) dialog.findViewById(R.id.txt_mobile);
                        txt_mobile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        Button btn_reschedule = (Button) dialog.findViewById(R.id.btn_reschedule);
                        btn_reschedule.setVisibility(View.VISIBLE);
                        btn_reschedule.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, AssignFactory.class);
                                intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                                intent.putExtra("completion", hashMapArrayList.get(posistion).get("completion"));
                                intent.putExtra("delivery_person_contact", hashMapArrayList.get(posistion).get("delivery_person_contact"));

                                context.startActivity(intent);
                                dialog.dismiss();
                            }
                        });

                        txt_completion.setText("75%");
                        txt_status.setText("Assigned To Factory " + hashMapArrayList.get(posistion).get("factory_name") + "Still Processing Not Completed Yet.");
                        txt_name.setText(hashMapArrayList.get(posistion).get("factory_person"));
                        txt_mobile.setText(hashMapArrayList.get(posistion).get("factory_contact"));
                        dialog.show();


                    } else if (hashMapArrayList.get(posistion).get("completion").equals("90")) {
                        Intent intent = new Intent(context, AssignFactory.class);
                        intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));

                        context.startActivity(intent);
                    } else if (hashMapArrayList.get(posistion).get("completion").equals("95")) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.status_final_popup);
                        TextView txt_completion = (TextView) dialog.findViewById(R.id.txt_completion);
                        TextView txt_status = (TextView) dialog.findViewById(R.id.txt_status);
                        TextView txt_name = (TextView) dialog.findViewById(R.id.txt_name);
                        TextView txt_mobile = (TextView) dialog.findViewById(R.id.txt_mobile);
                        TextView txt_factory_name = (TextView) dialog.findViewById(R.id.txt_factory_name);
                        TextView txt_pickup_name = (TextView) dialog.findViewById(R.id.txt_pickup_name);
                        TextView txt_delivery_name = (TextView) dialog.findViewById(R.id.txt_delivery_name);

                        TextView txt_factory_mobile = (TextView) dialog.findViewById(R.id.txt_factory_mobile);

                        txt_factory_name.setText(hashMapArrayList.get(posistion).get("factory_name"));
                        txt_pickup_name.setText(hashMapArrayList.get(posistion).get("delivery_contact_name"));
                        txt_delivery_name.setText(hashMapArrayList.get(posistion).get("delivery_assign_person"));

                        txt_factory_mobile.setText(hashMapArrayList.get(posistion).get("factory_contact"));


                        txt_mobile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        Button btn_reschedule = (Button) dialog.findViewById(R.id.btn_reschedule);
                        btn_reschedule.setVisibility(View.VISIBLE);
                        btn_reschedule.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, AssignFactory.class);
                                intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                                dialog.dismiss();

                                context.startActivity(intent);
                            }
                        });

                        txt_completion.setText("95%");
                        txt_status.setText("Ready To Delivery " + hashMapArrayList.get(posistion).get("unique_code") + '\n' + "Assigned To" +
                                hashMapArrayList.get(posistion).get("delivery_contact_name") + '\n' + "And Not Deliverd Yet.");
                        txt_name.setText(hashMapArrayList.get(posistion).get("delivery_contact_name"));
                        txt_mobile.setText(hashMapArrayList.get(posistion).get("delivery_contact"));
                        dialog.show();
                    } else {

                        final Intent intent = new Intent(context, AssignDealer.class);
                        intent.putExtra("shop_id", Configg.getDATA(context, "sid"));
                        intent.putExtra("shop_keeper", Configg.getDATA(context, "shop_keeper"));
                        intent.putExtra("mobile", Configg.getDATA(context, "mobile"));

                        intent.putExtra("shop_code_and_name", Configg.getDATA(context, "shop_code_and_name"));

                        intent.putExtra("city", hashMapArrayList.get(posistion).get("city"));
                        intent.putExtra("locality", hashMapArrayList.get(posistion).get("locality"));
                        intent.putExtra("customer_id", hashMapArrayList.get(posistion).get("customer_id"));
                        intent.putExtra("mobile", hashMapArrayList.get(posistion).get("mobile"));
                        intent.putExtra("pickup_date", hashMapArrayList.get(posistion).get("pickup_date"));
                        intent.putExtra("pickup_time", hashMapArrayList.get(posistion).get("pickup_time"));
                        intent.putExtra("mobile_date", hashMapArrayList.get(posistion).get("mobile_date"));
                        intent.putExtra("city_id", hashMapArrayList.get(posistion).get("city_id"));
                        intent.putExtra("locality_id", hashMapArrayList.get(posistion).get("locality_id"));
                        intent.putExtra("delivery_id", hashMapArrayList.get(posistion).get("delivery_id"));
                        intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                        intent.putExtra("delivery_person", hashMapArrayList.get(posistion).get("delivery_person"));
                        intent.putExtra("completion", hashMapArrayList.get(posistion).get("completion"));
                        intent.putExtra("delivery_contact_name", hashMapArrayList.get(posistion).get("delivery_contact_name"));

                        intent.putExtra("address", hashMapArrayList.get(posistion).get("address"));
                        intent.putExtra("unique_code", hashMapArrayList.get(posistion).get("unique_code"));

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
                        final String currentDateTime = dateFormat.format(new Date()); // Find todays dat
                        String s[] = hashMapArrayList.get(posistion).get("pickup_date").split("-");
                        String s2[] = currentDateTime.split("-");

                        Date date1 = new Date(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                        Date date2 = new Date(Integer.parseInt(s2[0]), Integer.parseInt(s2[1]), Integer.parseInt(s2[2]));

                        // make 3 comparisons with them
                        int comparison = date1.compareTo(date2);
//                        int comparison2 = date2.compareTo(date);
//                        int comparison3 = date.compareTo(date);
                        if (comparison == 0) {

                            context.startActivity(intent);


                        } else {

                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.confirmation);
                            Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
                            Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
                            TextView txt_staus = (TextView) dialog.findViewById(R.id.txt_staus);
                            txt_staus.setText("Request Date is " + hashMapArrayList.get(posistion).get("pickup_date")
                                    + '\n' + "Anyway Assign Today.");

                            btn_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    context.startActivity(intent);
                                    dialog.dismiss();

                                }
                            });
                            btn_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                    context.startActivity(intent);
                                    dialog.dismiss();

                                }
                            });
                            dialog.show();
                            Toast.makeText(context, "The Pickup Date Is : " + hashMapArrayList.get(posistion).get("pickup_date"), Toast.LENGTH_SHORT).show();


                        }
//                        context.startActivity(intent);
                    }
                } else if (Configg.getDATA(context, "type").equals("delivery")) {
                    if (hashMapArrayList.get(posistion).get("completion").equals("25")) {

                        Intent intent = new Intent(context, DeliveryPickupCustomer.class);
                        intent.putExtra("city", hashMapArrayList.get(posistion).get("city"));
                        intent.putExtra("locality", hashMapArrayList.get(posistion).get("locality"));
                        intent.putExtra("cid", hashMapArrayList.get(posistion).get("cid"));
                        intent.putExtra("shop_id", hashMapArrayList.get(posistion).get("shop_id"));

                        intent.putExtra("mobile", hashMapArrayList.get(posistion).get("mobile"));
                        intent.putExtra("pickup_date", hashMapArrayList.get(posistion).get("pickup_date"));
                        intent.putExtra("pickup_time", hashMapArrayList.get(posistion).get("pickup_time"));
                        intent.putExtra("mobile_date", hashMapArrayList.get(posistion).get("mobile_date"));
                        intent.putExtra("city_id", hashMapArrayList.get(posistion).get("city_id"));
                        intent.putExtra("locality_id", hashMapArrayList.get(posistion).get("locality_id"));
                        intent.putExtra("delivery_id", hashMapArrayList.get(posistion).get("delivery_id"));
                        intent.putExtra("booking_id", hashMapArrayList.get(posistion).get("booking_id"));
                        intent.putExtra("delivery_person", hashMapArrayList.get(posistion).get("delivery_person"));
                        intent.putExtra("address", hashMapArrayList.get(posistion).get("address"));
                        intent.putExtra("unique_code", hashMapArrayList.get(posistion).get("unique_code"));


                        context.startActivity(intent);
                    } else if (hashMapArrayList.get(posistion).get("completion").equals("50")) {

                    } else if (hashMapArrayList.get(posistion).get("completion").equals("75")) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.status_popup);
                        TextView txt_completion = (TextView) dialog.findViewById(R.id.txt_completion);
                        TextView txt_status = (TextView) dialog.findViewById(R.id.txt_status);
                        TextView txt_name = (TextView) dialog.findViewById(R.id.txt_name);
                        TextView txt_mobile = (TextView) dialog.findViewById(R.id.txt_mobile);
                        txt_mobile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        txt_mobile.setVisibility(View.GONE);
                        Button btn_reschedule = (Button) dialog.findViewById(R.id.btn_reschedule);
                        btn_reschedule.setVisibility(View.GONE);
                        btn_reschedule.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, AssignFactory.class);
                                context.startActivity(intent);
                                dialog.dismiss();
                            }
                        });

                        txt_completion.setText("75%");
                        txt_status.setText("Assigned To Factory " + hashMapArrayList.get(posistion).get("factory_name") + "Still Processing Not Completed Yet.");
                        txt_name.setText(hashMapArrayList.get(posistion).get("factory_person"));
                        txt_mobile.setText(hashMapArrayList.get(posistion).get("factory_contact"));
                        dialog.show();


                    } else if (hashMapArrayList.get(posistion).get("completion").equals("95")) {
                        Intent intent = new Intent(context, AssignFactory.class);
                        intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                        context.startActivity(intent);

                    }

                } else if (Configg.getDATA(context, "type").equals("factory")) {
                    if (hashMapArrayList.get(posistion).get("completion").equals("75")) {

                        Intent intent = new Intent(context, AssignFactory.class);
                        intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                        context.startActivity(intent);
                    } else if (hashMapArrayList.get(posistion).get("completion").equals("90")) {

                        Intent intent = new Intent(context, AssignFactory.class);
                        intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                        context.startActivity(intent);
                    }
                } else if (Configg.getDATA(context, "type").equals("admin")) {
/*
                    if (hashMapArrayList.get(posistion).get("completion").equals("50")) {
                        Intent intent = new Intent(context, AssignFactory.class);
                        intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                        context.startActivity(intent);


                    } else if (hashMapArrayList.get(posistion).get("completion").equals("25")) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.status_popup);
                        TextView txt_completion = (TextView) dialog.findViewById(R.id.txt_completion);
                        TextView txt_status = (TextView) dialog.findViewById(R.id.txt_status);
                        TextView txt_name = (TextView) dialog.findViewById(R.id.txt_name);
                        TextView txt_mobile = (TextView) dialog.findViewById(R.id.txt_mobile);
                        txt_mobile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        Button btn_reschedule = (Button) dialog.findViewById(R.id.btn_reschedule);

                        btn_reschedule.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, AssignDealer.class);
                                intent.putExtra("shop_id", Configg.getDATA(context, "sid"));
                                intent.putExtra("shop_keeper", Configg.getDATA(context, "shop_keeper"));
                                intent.putExtra("mobile", Configg.getDATA(context, "mobile"));

                                intent.putExtra("shop_code_and_name", Configg.getDATA(context, "shop_code_and_name"));
                                intent.putExtra("completion", hashMapArrayList.get(posistion).get("completion"));

                                intent.putExtra("delivery_contact_name", hashMapArrayList.get(posistion).get("delivery_contact_name"));


                                intent.putExtra("city", hashMapArrayList.get(posistion).get("city"));
                                intent.putExtra("locality", hashMapArrayList.get(posistion).get("locality"));
                                intent.putExtra("customer_id", hashMapArrayList.get(posistion).get("customer_id"));
                                intent.putExtra("mobile", hashMapArrayList.get(posistion).get("mobile"));
                                intent.putExtra("pickup_date", hashMapArrayList.get(posistion).get("pickup_date"));
                                intent.putExtra("pickup_time", hashMapArrayList.get(posistion).get("pickup_time"));
                                intent.putExtra("mobile_date", hashMapArrayList.get(posistion).get("mobile_date"));
                                intent.putExtra("city_id", hashMapArrayList.get(posistion).get("city_id"));
                                intent.putExtra("locality_id", hashMapArrayList.get(posistion).get("locality_id"));
                                intent.putExtra("delivery_id", hashMapArrayList.get(posistion).get("delivery_id"));
                                intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                                intent.putExtra("delivery_person", hashMapArrayList.get(posistion).get("delivery_person"));
                                intent.putExtra("address", hashMapArrayList.get(posistion).get("address"));
                                intent.putExtra("unique_code", hashMapArrayList.get(posistion).get("unique_code"));
                                dialog.dismiss();

                                context.startActivity(intent);
                            }
                        });

                        txt_completion.setText("25%");
                        txt_status.setText("Booking Code " + hashMapArrayList.get(posistion).get("unique_code") + '\n' + "Assigned To" +
                                hashMapArrayList.get(posistion).get("delivery_contact_name") + '\n' + "And Not Pickuped Yet.");
                        txt_name.setText(hashMapArrayList.get(posistion).get("delivery_contact_name"));
                        txt_mobile.setText(hashMapArrayList.get(posistion).get("delivery_contact"));
                        dialog.show();


                    } else if (hashMapArrayList.get(posistion).get("completion").equals("75")) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.status_popup);
                        TextView txt_completion = (TextView) dialog.findViewById(R.id.txt_completion);
                        TextView txt_status = (TextView) dialog.findViewById(R.id.txt_status);
                        TextView txt_name = (TextView) dialog.findViewById(R.id.txt_name);
                        TextView txt_mobile = (TextView) dialog.findViewById(R.id.txt_mobile);
                        txt_mobile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        Button btn_reschedule = (Button) dialog.findViewById(R.id.btn_reschedule);
                        btn_reschedule.setVisibility(View.VISIBLE);
                        btn_reschedule.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, AssignFactory.class);
                                intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));

                                context.startActivity(intent);
                                dialog.dismiss();
                            }
                        });

                        txt_completion.setText("75%");
                        txt_status.setText("Assigned To Factory " + hashMapArrayList.get(posistion).get("factory_name") + "Still Processing Not Completed Yet.");
                        txt_name.setText(hashMapArrayList.get(posistion).get("factory_person"));
                        txt_mobile.setText(hashMapArrayList.get(posistion).get("factory_contact"));
                        dialog.show();


                    } else if (hashMapArrayList.get(posistion).get("completion").equals("90")) {
                        Intent intent = new Intent(context, AssignFactory.class);
                        intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));

                        context.startActivity(intent);
                    } else if (hashMapArrayList.get(posistion).get("completion").equals("95")) {
                        Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.status_final_popup);
                        TextView txt_completion = (TextView) dialog.findViewById(R.id.txt_completion);
                        TextView txt_status = (TextView) dialog.findViewById(R.id.txt_status);
                        TextView txt_name = (TextView) dialog.findViewById(R.id.txt_name);
                        TextView txt_mobile = (TextView) dialog.findViewById(R.id.txt_mobile);
                        TextView txt_factory_name = (TextView) dialog.findViewById(R.id.txt_factory_name);
                        TextView txt_pickup_name = (TextView) dialog.findViewById(R.id.txt_pickup_name);
                        TextView txt_delivery_name = (TextView) dialog.findViewById(R.id.txt_delivery_name);

                        TextView txt_factory_mobile = (TextView) dialog.findViewById(R.id.txt_factory_mobile);

                        txt_factory_name.setText(hashMapArrayList.get(posistion).get("factory_name"));
                        txt_pickup_name.setText(hashMapArrayList.get(posistion).get("delivery_contact_name"));
                        txt_delivery_name.setText(hashMapArrayList.get(posistion).get("delivery_contact_name"));

                        txt_factory_mobile.setText(hashMapArrayList.get(posistion).get("factory_contact"));


                        txt_mobile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        Button btn_reschedule = (Button) dialog.findViewById(R.id.btn_reschedule);
                        btn_reschedule.setVisibility(View.GONE);

                        txt_completion.setText("95%");
                        txt_status.setText("Ready To Delivery " + hashMapArrayList.get(posistion).get("unique_code") + '\n' + "Assigned To" +
                                hashMapArrayList.get(posistion).get("delivery_contact_name") + '\n' + "And Not Deliverd Yet.");
                        txt_name.setText(hashMapArrayList.get(posistion).get("delivery_contact_name"));
                        txt_mobile.setText(hashMapArrayList.get(posistion).get("delivery_contact"));
                        dialog.show();
                    } else {

                        Intent intent = new Intent(context, AssignDealer.class);
                        intent.putExtra("shop_id", Configg.getDATA(context, "sid"));
                        intent.putExtra("shop_keeper", Configg.getDATA(context, "shop_keeper"));
                        intent.putExtra("mobile", Configg.getDATA(context, "mobile"));

                        intent.putExtra("shop_code_and_name", Configg.getDATA(context, "shop_code_and_name"));

                        intent.putExtra("city", hashMapArrayList.get(posistion).get("city"));
                        intent.putExtra("locality", hashMapArrayList.get(posistion).get("locality"));
                        intent.putExtra("customer_id", hashMapArrayList.get(posistion).get("customer_id"));
                        intent.putExtra("mobile", hashMapArrayList.get(posistion).get("mobile"));
                        intent.putExtra("pickup_date", hashMapArrayList.get(posistion).get("pickup_date"));
                        intent.putExtra("pickup_time", hashMapArrayList.get(posistion).get("pickup_time"));
                        intent.putExtra("mobile_date", hashMapArrayList.get(posistion).get("mobile_date"));
                        intent.putExtra("city_id", hashMapArrayList.get(posistion).get("city_id"));
                        intent.putExtra("locality_id", hashMapArrayList.get(posistion).get("locality_id"));
                        intent.putExtra("delivery_id", hashMapArrayList.get(posistion).get("delivery_id"));
                        intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                        intent.putExtra("delivery_person", hashMapArrayList.get(posistion).get("delivery_person"));
                        intent.putExtra("completion", hashMapArrayList.get(posistion).get("completion"));
                        intent.putExtra("delivery_contact_name", hashMapArrayList.get(posistion).get("delivery_contact_name"));


                        intent.putExtra("address", hashMapArrayList.get(posistion).get("address"));
                        intent.putExtra("unique_code", hashMapArrayList.get(posistion).get("unique_code"));


                        context.startActivity(intent);
                    }*/
                }


            }
        });


//        }
        return convertView;
    }

    private class MyViewHolder {
        private final TextView txt_date;
        private final TextView txt_unique_code;
        private final TextView txt_time, txt_assign, txt_city, txt_locality, txt_address, txt_mobile, txt_name;
        private final TextView txt_cancel, txt_amount;
        private View lltotal_amt;

        public MyViewHolder(View item) {
            txt_amount = (TextView) item.findViewById(R.id.txt_amount);
            lltotal_amt = item.findViewById(R.id.lltotal_amt);
            txt_unique_code = (TextView) item.findViewById(R.id.txt_unique_code);
            txt_time = (TextView) item.findViewById(R.id.txt_time);
            txt_assign = (TextView) item.findViewById(R.id.txt_assign);
            txt_date = (TextView) item.findViewById(R.id.txt_date);
            txt_city = (TextView) item.findViewById(R.id.txt_city);
            txt_locality = (TextView) item.findViewById(R.id.txt_locality);
            txt_address = (TextView) item.findViewById(R.id.txt_address);
            txt_mobile = (TextView) item.findViewById(R.id.txt_mobile);
            txt_cancel = (TextView) item.findViewById(R.id.txt_cancel);
            txt_name = (TextView) item.findViewById(R.id.txt_name);

        }
    }

    private void volley_cancel_booking(final String cancel,
                                       final String pcid

    ) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Configg.MAIN_URL + Configg.BOOKING_CANCEL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("1")) {
//                        ((Activity) context).finish();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        builder.setMessage("Booking Cancel").setTitle(jsonObject.getString("message"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // do nothing
                                        ((Activity) context).finish();
                                        ((Activity) context).overridePendingTransition(0, 0);
                                        context.startActivity(((Activity) context).getIntent());
                                        ((Activity) context).overridePendingTransition(0, 0);


                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                        alert.setCancelable(false);
                    } else if (jsonObject.getString("success").equals("0")) {
                        ((Activity) context).finish();
//                        Config.alert("Booking Cancel", jsonObject.getString("message"), 0, context);
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

                params.put("cancel", cancel);
                params.put("pcid", pcid);


//                params.put("comment", Uri.encode(comment));
//                params.put("comment_post_ID",String.valueOf(postId));
//                params.put("blogId",String.valueOf(blogId));
                System.out.println("params" + params.toString());

                Log.w("params_out", params.toString());

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

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        hashMapArrayList.clear();
        if (charText.length() == 0) {
            hashMapArrayList.addAll(hashMapArrayList2);
        } else {
            for (HashMap<String, String> wp : hashMapArrayList2) {
                if (wp.get("unique_code").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }
                if (wp.get("mobile").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }
                if (wp.get("locality").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }
                if (wp.get("city").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }

                if (wp.get("address").toLowerCase(Locale.getDefault()).contains(charText)) {
                    hashMapArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}
