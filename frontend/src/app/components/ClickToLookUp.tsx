import React, { ReactNode } from "react";
import axios from "axios";
import { notification } from "antd";

const ClickToLookUp = ({ children }: { children: ReactNode }) => {
  const [api, contextHolder] = notification.useNotification();

  // 双击处理函数
  const handleDoubleClick = () => {
    const selection = window.getSelection();
    if (selection?.toString()) {
      const word = selection.toString().trim();
      fetchWordDefinition(word);
    }
  };

  // 查询单词定义函数
  const fetchWordDefinition = (word: string) => {
    const apiUrl = `https://api.dictionaryapi.dev/api/v2/entries/en/${word}`;
    axios
      .get(apiUrl)
      .then((res: any) => {
        if (!res.data) {
          return;
        }
        const data = res.data;
        if (data && data[0]) {
          api.info({
            message: word,
            description: data[0].meanings[0].definitions[0].definition,
            duration: 0,
          });
        } else {
          api.info({
            message: word,
            description: "No definition found",
            duration: 0,
          });
        }
      })
      .catch((reason) => {
        console.log("Error fetching definition", reason);
      });
  };

  return (
    <span onDoubleClick={handleDoubleClick}>
      <span>{children}</span>
      {contextHolder}
    </span>
  );
};

export default ClickToLookUp;
