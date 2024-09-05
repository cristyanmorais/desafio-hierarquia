"use client";

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import styles from './HomePage.module.css';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faDownload } from '@fortawesome/free-solid-svg-icons';

const HomePage: React.FC = () => {
  const [hierarchy, setHierarchy] = useState<any>({});
  const [levels, setLevels] = useState<any[]>([]);

  useEffect(() => {
    getHierarchy();
  }, []);

  const addSubLevel = (parentLevel: any, parentKey: string) => {
    const newLevelName = prompt("Digite o nome do novo nível:");
    if (newLevelName) {
      if (!parentLevel[parentKey]) {
        parentLevel[parentKey] = {};
      }
      parentLevel[parentKey][newLevelName] = {};
      setHierarchy({ ...hierarchy });
    }
  };

  const getHierarchy = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/hierarchy/get');
      setHierarchy(response.data);
    } catch (error) {
      console.error('Erro ao obter hierarquia:', error);
    }
  };

  const saveHierarchy = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/hierarchy/save', { hierarchy });
      alert(response.data);
    } catch (error) {
      console.error('Erro ao salvar hierarquia:', error);
    }
  };

  const downloadHierarchy = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/hierarchy/download', {
        responseType: 'blob',
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'hierarquia.json');
      document.body.appendChild(link);
      link.click();
    } catch (error) {
      console.error('Erro ao baixar hierarquia:', error);
    }
  };

   const renderHierarchy = (data: any) => {
    if (!data || typeof data !== 'object') {
      return null;
    }

    return (
      <ul className={styles.hierarchy}>
        {Object.entries(data).map(([key, value]) => (
          <li key={key}>
            <div className={styles.node}>
              <button onClick={() => addSubLevel(data, key)}>{key}</button>
            </div>
            {renderHierarchy(value)}
          </li>
        ))}
      </ul>
    );
  };

  return (
    <div className={styles.container}>
      <h1>Hierarquia de Palavras</h1>

      <div>
        {renderHierarchy(hierarchy)}
      </div>

      <div className={styles.buttons}>
        <button className={styles.savebtn} onClick={saveHierarchy}>Salvar</button>
        <button className={styles.dwbtn} onClick={downloadHierarchy}><FontAwesomeIcon icon={faDownload} /></button>
      </div>
    </div>
  );
};

export default HomePage;
