import React, { useEffect } from "react";
import { Button, Form, Input, Space, message } from "antd";
import axios from "axios";

const UserSettings: React.FC<{
  userInfo: any;
  onSuccess: () => void;
}> = ({ userInfo, onSuccess }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldsValue({
      firstName: userInfo.firstName,
      lastName: userInfo.lastName,
    });
  }, [userInfo]);

  const onFinish = (values: any) => {
    console.log(values);
    axios
      .post("http://localhost:8088/user/update", {
        firstName: values.firstName,
        lastName: values.lastName,
      })
      .then(() => {
        message.success("Success!");
        onSuccess();
      })
      .catch((reason) => {
        console.log("update user info fail:", reason);
      });
  };

  const layout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 10 },
  };

  const tailLayout = {
    wrapperCol: { offset: 8, span: 10 },
  };

  return (
    <Form {...layout} form={form} onFinish={onFinish}>
      <Form.Item
        name="firstName"
        label="First Name"
        rules={[{ required: true }]}
      >
        <Input />
      </Form.Item>
      <Form.Item name="lastName" label="Last Name" rules={[{ required: true }]}>
        <Input />
      </Form.Item>

      <Form.Item {...tailLayout}>
        <Space>
          <Button type="primary" htmlType="submit">
            Submit
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default UserSettings;
