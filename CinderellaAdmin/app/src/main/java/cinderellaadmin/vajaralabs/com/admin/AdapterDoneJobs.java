package cinderellaadmin.vajaralabs.com.admin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.Configg;

/**
 * Created by Karthik S on 8/24/2016.
 */
public class AdapterDoneJobs extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterDoneJobs(Context context, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

            convertView = inflater.inflate(R.layout.done_list, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        mViewHolder.txt_delivery_name.setText(hashMapArrayList.get(posistion).get("delivery_contact_name"));
        mViewHolder.txt_tag_no.setText(hashMapArrayList.get(posistion).get("tag_no"));
        mViewHolder.txt_customer_name.setText(hashMapArrayList.get(posistion).get("customer_name"));
        mViewHolder.txt_clothes.setText(hashMapArrayList.get(posistion).get("items"));
        mViewHolder.txt_total_amt.setText(hashMapArrayList.get(posistion).get("overall_total"));
        mViewHolder.txt_amount.setText(hashMapArrayList.get(posistion).get("amount"));
        mViewHolder.txt_date.setText(hashMapArrayList.get(posistion).get("pickup_date"));
        mViewHolder.txt_date.setText(hashMapArrayList.get(posistion).get("pickup_date"));
        mViewHolder.txt_unique_code.setText(hashMapArrayList.get(posistion).get("unique_code"));
        mViewHolder.txt_time.setText(hashMapArrayList.get(posistion).get("pickup_time"));
        mViewHolder.txt_city.setText(hashMapArrayList.get(posistion).get("city"));
        mViewHolder.txt_locality.setText(hashMapArrayList.get(posistion).get("locality"));
        mViewHolder.txt_address.setText(hashMapArrayList.get(posistion).get("address"));
        mViewHolder.txt_mobile.setText(hashMapArrayList.get(posistion).get("mobile"));
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
                mViewHolder.txt_assign.setText("Assign To Delivery Person  ");


            }
        }
        mViewHolder.txt_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Configg.getDATA(context, "type").equals("shop")) {

                    if (hashMapArrayList.get(posistion).get("completion").equals("50")) {
                        Intent intent = new Intent(context, AssignFactory.class);
                        intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                        context.startActivity(intent);


                    } else if (hashMapArrayList.get(posistion).get("completion").equals("25")) {
                        Dialog dialog = new Dialog(context);
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

                        txt_completion.setText("25%");
                        txt_status.setText("Booking Code " + hashMapArrayList.get(posistion).get("unique_code") + '\n' + "Assigned To" +
                                hashMapArrayList.get(posistion).get("delivery_contact_name") + '\n' + "And Not Pickuped Yet.");
                        txt_name.setText(hashMapArrayList.get(posistion).get("delivery_contact_name"));
                        txt_mobile.setText(hashMapArrayList.get(posistion).get("delivery_contact"));
                        dialog.show();


                    } else if (hashMapArrayList.get(posistion).get("completion").equals("75")) {
                        Dialog dialog = new Dialog(context);
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
                        intent.putExtra("address", hashMapArrayList.get(posistion).get("address"));
                        intent.putExtra("unique_code", hashMapArrayList.get(posistion).get("unique_code"));


                        context.startActivity(intent);
                    }
                } else if (Configg.getDATA(context, "type").equals("delivery")) {
                    if (hashMapArrayList.get(posistion).get("completion").equals("25")) {

                        Intent intent = new Intent(context, DeliveryPickupCustomer.class);
                        intent.putExtra("city", hashMapArrayList.get(posistion).get("city"));
                        intent.putExtra("locality", hashMapArrayList.get(posistion).get("locality"));
                        intent.putExtra("cid", hashMapArrayList.get(posistion).get("cid"));
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
                        Dialog dialog = new Dialog(context);
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

                    if (hashMapArrayList.get(posistion).get("completion").equals("50")) {
                        Intent intent = new Intent(context, AssignFactory.class);
                        intent.putExtra("pcid", hashMapArrayList.get(posistion).get("pcid"));
                        context.startActivity(intent);


                    } else if (hashMapArrayList.get(posistion).get("completion").equals("25")) {
                        Dialog dialog = new Dialog(context);
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

                        txt_completion.setText("25%");
                        txt_status.setText("Booking Code " + hashMapArrayList.get(posistion).get("unique_code") + '\n' + "Assigned To" +
                                hashMapArrayList.get(posistion).get("delivery_contact_name") + '\n' + "And Not Pickuped Yet.");
                        txt_name.setText(hashMapArrayList.get(posistion).get("delivery_contact_name"));
                        txt_mobile.setText(hashMapArrayList.get(posistion).get("delivery_contact"));
                        dialog.show();


                    } else if (hashMapArrayList.get(posistion).get("completion").equals("75")) {
                        Dialog dialog = new Dialog(context);
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
                        intent.putExtra("address", hashMapArrayList.get(posistion).get("address"));
                        intent.putExtra("unique_code", hashMapArrayList.get(posistion).get("unique_code"));


                        context.startActivity(intent);
                    }
                }


            }
        });


//        }
        return convertView;
    }

    private class MyViewHolder {
        private final TextView txt_date;
        private final TextView txt_unique_code;
        private final TextView txt_time, txt_assign, txt_city, txt_locality, txt_address, txt_mobile;
        public TextView txt_tag_no;
        public TextView txt_clothes, txt_amount, txt_total_amt, txt_delivery_name, txt_customer_name;

        public MyViewHolder(View item) {
            txt_unique_code = (TextView) item.findViewById(R.id.txt_unique_code);
            txt_customer_name = (TextView) item.findViewById(R.id.txt_customer_name);
            txt_time = (TextView) item.findViewById(R.id.txt_time);
            txt_assign = (TextView) item.findViewById(R.id.txt_assign);
            txt_date = (TextView) item.findViewById(R.id.txt_date);
            txt_city = (TextView) item.findViewById(R.id.txt_city);
            txt_locality = (TextView) item.findViewById(R.id.txt_locality);
            txt_address = (TextView) item.findViewById(R.id.txt_address);
            txt_mobile = (TextView) item.findViewById(R.id.txt_mobile);
            txt_tag_no = (TextView) item.findViewById(R.id.txt_tag_no);
            txt_clothes = (TextView) item.findViewById(R.id.txt_clothes);
            txt_total_amt = (TextView) item.findViewById(R.id.txt_total_amt);
            txt_amount = (TextView) item.findViewById(R.id.txt_amount_received);
            txt_delivery_name = (TextView) item.findViewById(R.id.txt_delivery_name);


        }
    }


}
