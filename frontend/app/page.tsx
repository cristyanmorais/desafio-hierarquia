"use client";

import React, { useEffect, useState } from 'react';
import axios from 'axios';

const HomePage: React.FC = () => {
  const [hierarchy, setHierarchy] = useState<any>({});

  useEffect(() => {
    getHierarchy();
  }, []);

  const getHierarchy = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/hierarchy/get');
      setHierarchy(response.data);
    } catch (error) {
      console.error('Erro ao obter hierarquia:', error);
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

  const renderHierarchy = (data: any, level = 0) => {
    if (typeof data !== 'object' || data === null) {
      return <span>{data}</span>;
    }

    return (
      <ul style={{ paddingLeft: `${level * 10}px` }}>
        {Object.entries(data).map(([key, value], index) => (
          <li key={index}>
            <strong>{key}:</strong> {renderHierarchy(value, level + 1)}
          </li>
        ))}
      </ul>
    );
  };

  return (
    <div>
      <h1>Gerenciador de Hierarquia de Palavras</h1>
      
      <button onClick={downloadHierarchy}>Baixar Hierarquia</button>

      <div>
        <h2>Hierarquia Salva (JSON):</h2>
        {renderHierarchy(hierarchy)}
      </div>
    </div>
  );
};

export default HomePage;
