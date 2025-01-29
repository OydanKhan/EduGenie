"use client";
import React, { useEffect, useState } from "react";
import { Inter } from "next/font/google";
import { AntdRegistry } from "@ant-design/nextjs-registry";
import { useRouter } from "next/navigation";
import { Avatar, Button, Dropdown, Layout, Menu, Space } from "antd";
import type { MenuProps } from "antd";
const { Header, Content } = Layout;
import "./globals.css";
import styles from "@/app/page.module.css";
import Link from "next/link";
import axios from "axios";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { DownOutlined } from "@ant-design/icons";
import SignInButton from "@/app/components/SignInButton";
import { User } from "@/types";

const inter = Inter({ subsets: ["latin"] });

// set Axios interceptor
axios.interceptors.request.use(
  function (config) {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  function (error) {
    return Promise.reject(error);
  },
);

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const router = useRouter();
  const [current, setCurrent] = useState("");
  const [userInfo, setUserInfo] = useState<User | null>(null);

  useEffect(() => {
    setCurrent(window.location.pathname);
  }, []);

  const onClick: MenuProps["onClick"] = (e) => {
    setCurrent(e.key);
  };

  const goHome = () => {
    setCurrent("");
    location.href = "/";
  };

  const handleSignIn = (userInfo: User) => {
    setUserInfo(userInfo);
  };

  const handleClickSignIn = (userInfo: User) => {
    // setUserInfo(userInfo);
    location.reload();
  };

  const handleSignOut = async () => {
    localStorage.setItem("token", "");
    setUserInfo(null);
    goHome();
  };

  const userMenuItems: MenuProps["items"] = [
    {
      key: "/profile",
      label: <Link href="/profile">Profile</Link>,
    },
    {
      type: "divider",
    },
    {
      key: "/sign-out",
      label: <Button onClick={handleSignOut}>Sign out</Button>,
    },
  ];

  return (
    <GoogleOAuthProvider clientId="18188062495-splm7ss0dp13pspgsd3fftl8eqse35je.apps.googleusercontent.com">
      <html lang="en" className={inter.className}>
        <body>
          <AntdRegistry>
            <Layout style={{ minHeight: "100vh", backgroundColor: "#f0f2f5" }}>
              <Header
                className={styles.header}
                style={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "space-between",
                  backgroundColor: "#001529",
                  padding: "0 40px", // Increased padding for more spacing
                  height: "70px", // Slightly increased height
                }}
              >
                <div className={styles.logo} onClick={goHome}>
                  <Avatar
                    size={64}
                    // shape="square"
                    src={<img src={"/logo.jpeg"} alt="avatar" />}
                  />
                </div>
                <Menu
                  theme="dark"
                  mode="horizontal"
                  selectedKeys={[current]}
                  onClick={onClick}
                  items={[
                    {
                      label: <Link href="/practise">Practise</Link>,
                      key: "/practise",
                    },
                    {
                      label: <Link href="/course">Course</Link>,
                      key: "/course",
                    },
                    {
                      key: "/profile",
                      label: <Link href="/profile">Profile</Link>,
                    },
                  ]}
                  style={{
                    flex: 1,
                    display: "flex",
                    justifyContent: "center",
                    padding: "0 20px", // Extra padding between menu items
                  }}
                />
                {userInfo ? (
                  <Dropdown menu={{ items: userMenuItems }}>
                    <a onClick={(e) => e.preventDefault()}>
                      <Space>
                        Welcome, {userInfo.firstName}!&nbsp;
                        <DownOutlined />
                      </Space>
                    </a>
                  </Dropdown>
                ) : (
                  <SignInButton onAutoSignInSuccess={handleSignIn} onClickSignInSuccess={handleClickSignIn} size="middle">
                    Sign In
                  </SignInButton>
                )}
              </Header>
              <Content
                style={{
                  padding: "60px", // Increased padding for more content space
                  backgroundColor: "#fff",
                  borderRadius: "12px", // Slightly more rounded corners
                  margin: "30px", // More margin for outer spacing
                  boxShadow: "0 6px 18px rgba(0, 0, 0, 0.15)", // Slightly stronger shadow for depth
                }}
              >
                {children}
              </Content>
            </Layout>
          </AntdRegistry>
        </body>
      </html>
    </GoogleOAuthProvider>
  );
}
